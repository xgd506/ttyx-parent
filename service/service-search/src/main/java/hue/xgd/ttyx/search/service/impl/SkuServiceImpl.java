package hue.xgd.ttyx.search.service.impl;

import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.enums.SkuType;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.search.repositor.SkuRepository;
import hue.xgd.ttyx.search.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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
}
