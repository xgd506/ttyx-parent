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
import hue.xgd.ttyx.model.order.CartInfo;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.vo.activity.CouponRuleVo;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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
    //查询当前用户能够使用的优惠券，并找出最佳优惠券，一次只能一张
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        //获取全部用户优惠券
        List<CouponInfo> userAllCouponInfoList = baseMapper.selectCartCouponInfoList(userId);
        if(CollectionUtils.isEmpty(userAllCouponInfoList)) return null;

        //获取优惠券id列表
        List<Long> couponIdList = userAllCouponInfoList.stream().map(couponInfo -> couponInfo.getId()).collect(Collectors.toList());
        //查询优惠券对应的范围
        List<CouponRange> couponRangesList = rangeMapper.selectList(new LambdaQueryWrapper<CouponRange>().in(CouponRange::getCouponId, couponIdList));
        //获取优惠券id对应的满足使用范围的购物项skuId列表
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangesList);
        //优惠后减少金额
        BigDecimal reduceAmount = new BigDecimal("0");
        //记录最优优惠券
        CouponInfo optimalCouponInfo = null;
        for(CouponInfo couponInfo : userAllCouponInfoList) {
            if(CouponRangeType.ALL == couponInfo.getRangeType()) {
                //全场通用
                //判断是否满足优惠使用门槛
                //计算购物车商品的总价
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            } else {
                //优惠券id对应的满足使用范围的购物项skuId列表
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
                //当前满足使用范围的购物项
                List<CartInfo> currentCartInfoList = cartInfoList.stream().filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId())).collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect().intValue() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        if(null != optimalCouponInfo) {
            optimalCouponInfo.setIsOptimal(1);
        }
        return userAllCouponInfoList;

    }
    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    /**
     * 获取优惠券范围对应的购物车列表
     * @param cartInfoList
     * @param couponId
     * @return
     */
//    @Override
//    public CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId) {
//        CouponInfo couponInfo = this.getById(couponId);
//        if(null == couponInfo || couponInfo.getCouponStatus().intValue() == 2) return null;
//
//        //查询优惠券对应的范围
//        List<CouponRange> couponRangesList = rangeMapper.selectList(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, couponId));
//        //获取优惠券id对应的满足使用范围的购物项skuId列表
//        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangesList);
//        List<Long> skuIdList = couponIdToSkuIdMap.entrySet().iterator().next().getValue();
//        couponInfo.setSkuIdList(skuIdList);
//        return couponInfo;
//    }

    /**
     * 获取优惠券id对应的满足使用范围的购物项skuId列表
     * 说明：一个优惠券可能有多个购物项满足它的使用范围，那么多个购物项可以拼单使用这个优惠券
     * @param cartInfoList
     * @param couponRangesList
     * @return
     */
    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangesList) {
        Map<Long, List<Long>> couponIdToSkuIdMap = new HashMap<>();
        //优惠券id对应的范围列表
        Map<Long, List<CouponRange>> couponIdToCouponRangeListMap = couponRangesList.stream().collect(Collectors.groupingBy(couponRange -> couponRange.getCouponId()));
        Iterator<Map.Entry<Long, List<CouponRange>>> iterator = couponIdToCouponRangeListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<CouponRange>> entry = iterator.next();
            Long couponId = entry.getKey();
            List<CouponRange> couponRangeList = entry.getValue();

            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for(CouponRange couponRange : couponRangeList) {
                    if(CouponRangeType.SKU == couponRange.getRangeType() && couponRange.getRangeId().longValue() == cartInfo.getSkuId().intValue()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    } else if(CouponRangeType.CATEGORY == couponRange.getRangeType() && couponRange.getRangeId().longValue() == cartInfo.getCategoryId().intValue()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    } else {

                    }
                }
            }
            couponIdToSkuIdMap.put(couponId, new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;
    }
}
