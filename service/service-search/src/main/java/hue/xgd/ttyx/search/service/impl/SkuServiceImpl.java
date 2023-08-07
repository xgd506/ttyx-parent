package hue.xgd.ttyx.search.service.impl;

import hue.xgd.ttyx.client.activity.ActivityFeignClient;
import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.common.security.AuthContextHolder;
import hue.xgd.ttyx.enums.SkuType;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.search.repositor.SkuRepository;
import hue.xgd.ttyx.search.service.SkuService;
import hue.xgd.ttyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:00
 * @Description:
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Resource
    private SkuRepository skuRepository;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private ActivityFeignClient activityFeignClient;
    @Override
    public void upperSku(Long skuId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo == null) return;
        Category category = productFeignClient.getCategory(skuId);
        //将信息封装到Es的实体中
        SkuEs skuEs = new SkuEs();
        if (!StringUtils.isEmpty(category)) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        //sku信息
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName() + "," + skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if (skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 秒杀
        }
        SkuEs save = skuRepository.save(skuEs);
    }

    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

    @Override
    public void deleteSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

    @Override
    public void BatchdDleteSku(List<Long> skuId) {
        for (Long id: skuId
             ) {
            skuRepository.deleteById(id);
        }
    }
    //小程序需要查询爆款商品
    @Override
    public List<SkuEs> findHotSkuList() {
        //spring Data  自动按照关键字去查询Es
        Pageable pageable= PageRequest.of(0,10);
        Page<SkuEs> pageModel= skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();

        return skuEsList;
    }

    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo searchParamVo) {
        //获得仓库Id，当前仓库是否还有库存
        Long wareId = AuthContextHolder.getWareId();
        searchParamVo.setWareId(wareId);
        //根据categoryId+keyWord+wareId进行查询
        Page<SkuEs> pageModel=null;
        //keyword不为空
        if(!StringUtils.isEmpty(searchParamVo.getKeyword())){
            pageModel=skuRepository.findByWareIdAndSkuNameContaining(
                   wareId,searchParamVo.getKeyword(),pageable );
        }else {
            //为空
            pageModel=skuRepository.findByCategoryIdAndWareId(searchParamVo.getCategoryId(),
                                                              wareId,pageable);
        }
        //查询优惠商品
        List<SkuEs> skuEsList = pageModel.getContent();
        if(!CollectionUtils.isEmpty(skuEsList)){
            List<Long> skuIdList = skuEsList.stream().map(item -> item.getId())
                                                     .collect(Collectors.toList());
            //远程调用service-activity查询优惠商品
            //一个商品只能参加一个活动，但是一个活动有多个规则
            //Map<Long,List<String>>
            Map<Long,List<String>> skuIdToRuleListMap=activityFeignClient.findActivity(skuIdList);
            if(skuIdToRuleListMap!=null){
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        return pageModel;
    }
}
