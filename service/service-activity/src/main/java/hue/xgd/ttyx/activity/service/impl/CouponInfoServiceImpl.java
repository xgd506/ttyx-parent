package hue.xgd.ttyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.activity.mapper.ActivityInfoMapper;
import hue.xgd.ttyx.activity.mapper.CouponInfoMapper;
import hue.xgd.ttyx.activity.mapper.CouponRangeMapper;
import hue.xgd.ttyx.activity.mapper.CouponUseMapper;
import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.activity.service.CouponInfoService;
import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.enums.CouponRangeType;
import hue.xgd.ttyx.model.activity.ActivityRule;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.activity.CouponRange;
import hue.xgd.ttyx.model.activity.CouponUse;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.vo.activity.CouponRuleVo;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:38
 * @Description:
 */
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {
    @Resource
    private CouponRangeMapper rangeMapper;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private CouponUseMapper couponUseMapper;
    @Resource
    private ActivityInfoService activityInfoService;
    @Override
    public IPage<CouponInfo> selectPage(Page<CouponInfo> pageParam) {
        Page<CouponInfo> couponInfoPage = baseMapper.selectPage(pageParam, null);
        List<CouponInfo> records =couponInfoPage.getRecords();
        records.stream().forEach(item->{
            item.setCouponTypeString(item.getCouponType().getComment());
            CouponRangeType rangeType = item.getRangeType();
            if(rangeType!=null){
                item.setRangeTypeString(item.getRangeType().getComment());
            }
        });
        return couponInfoPage;
    }

    @Override
    public CouponInfo getCouponInfo(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        CouponRangeType rangeType = couponInfo.getRangeType();
        if(rangeType!=null){
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        //根据id查询优惠券基本信息
        CouponInfo couponInfo = baseMapper.selectById(id);
        //判断优惠券使用类型==> coupon_range--range_id
        List<CouponRange> couponRanges = rangeMapper.selectList(
                new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id));
        List<Long> rangeIdList = couponRanges.stream().map(CouponRange::getRangeId)
                                                  .collect(Collectors.toList());
        Map<String,Object> result=new HashMap<>();
        if(!CollectionUtils.isEmpty(rangeIdList)){
           
            if(couponInfo.getRangeType()==CouponRangeType.SKU){
                //sku类型---》skuId
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                result.put("skuInfoList",skuInfoList);
            }else if(couponInfo.getRangeType()==CouponRangeType.CATEGORY){
                //category--》categoryId
              List<Category> categoryList =productFeignClient.findCategoryList(rangeIdList);
              result.put("categoryList",categoryList);
            }
        }

        return result;
    }

    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        rangeMapper.delete(new LambdaQueryWrapper<CouponRange>()
                .eq(CouponRange::getCouponId,couponRuleVo.getCouponId()));
        CouponInfo couponInfo = baseMapper.selectById(couponRuleVo.getCouponId());
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());
        baseMapper.updateById(couponInfo);

        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange :couponRangeList) {
            couponRange.setCouponId(couponRuleVo.getCouponId());
            rangeMapper.insert(couponRange);
        }
    }

    @Override
    public Object findCouponByKeyword(String keyword) {
        QueryWrapper<CouponInfo> couponInfoQueryWrapper = new QueryWrapper<>();
        couponInfoQueryWrapper.like("coupon_name",keyword);
        return baseMapper.selectList(couponInfoQueryWrapper);
    }

    @Override
    public Map<String, Object> getCouponInfoByUserId(Long skuId,Long userId) {
        Map<String,Object> result=new HashMap<>();
        //得到分类categoryId 判断是否属于优惠返回
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //获得优惠券信息
        List<CouponInfo> couponInfoList= baseMapper.selectCouponInfoList(skuInfo,skuInfo.getCategoryId(),userId);
        /* 没有考虑到商品是否属于优惠券可用范围
        List<CouponUse> couponUseList= couponUseMapper.getCouponInfoByUseId(userId);

        List<Long> couponIdList = couponUseList.stream().map(CouponUse::getCouponId)
                                                    .collect(Collectors.toList());
        List<CouponInfo> couponInfoList=new ArrayList<>();
        for (Long couponId:couponIdList) {
            CouponInfo couponInfo = baseMapper.selectById(couponId);
            couponInfoList.add(couponInfo);
        }
         */
        result.put("couponInfoList",couponInfoList);
        Map<String, Object> activityRuleList = activityInfoService.findActivityRuleList(skuId);
        result.putAll(activityRuleList);
        return result;
    }
}
