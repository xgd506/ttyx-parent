package hue.xgd.ttyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.activity.mapper.ActivityRuleMapper;
import hue.xgd.ttyx.activity.service.CouponInfoService;
import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.activity.mapper.ActivityInfoMapper;
import hue.xgd.ttyx.activity.mapper.ActivitySkuMapper;
import hue.xgd.ttyx.enums.ActivityType;
import hue.xgd.ttyx.model.activity.ActivityInfo;
import hue.xgd.ttyx.model.activity.ActivityRule;
import hue.xgd.ttyx.model.activity.ActivitySku;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.order.CartInfo;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.vo.activity.ActivityRuleVo;
import hue.xgd.ttyx.vo.order.CartInfoVo;
import hue.xgd.ttyx.vo.order.OrderConfirmVo;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:33
 * @Description:
 */
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {
    @Resource
    private ActivityRuleMapper activityRuleMapper;
    @Resource
    private ActivitySkuMapper activitySkuMapper;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private CouponInfoService couponInfoService;
    @Override
    public IPage<ActivityInfo> selectAdminPage(Page<ActivityInfo> pageParams) {
        Page<ActivityInfo> activityInfoPage = baseMapper.selectPage(pageParams, null);
        List<ActivityInfo> records = activityInfoPage.getRecords();
        records.stream().forEach(item->{
            item.setActivityTypeString(item.getActivityType().getComment());
        });

        return activityInfoPage;
    }

    @Override
    public Map<String, Object> findActivityRuleList(Long id) {
        Map<String,Object> result=new HashMap<>();
        //获取活动规则
        LambdaQueryWrapper<ActivityRule> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRule::getActivityId,id);
        List<ActivityRule> activityRules = activityRuleMapper.selectList(wrapper);
        result.put("activityRuleList",activityRules);
        //获取活动商品sku,通过远程调用Service-product接口;
        LambdaQueryWrapper<ActivitySku> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ActivitySku::getActivityId,id);
        //里面只有id
        List<ActivitySku> activitySkus = activitySkuMapper.selectList(queryWrapper);
        List<Long> skuIdList = activitySkus.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());

        //远程调用
        List<SkuInfo> skuInfoList=productFeignClient.findSkuInfoList(skuIdList);
        result.put("skuInfoList",skuInfoList);
        return result;
    }

    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        Long activityId = activityRuleVo.getActivityId();
        //保存前先删除原有的规则
        activityRuleMapper.delete(new LambdaQueryWrapper<ActivityRule>().
                eq(ActivityRule::getActivityId,activityId));
        //删除原有的商品信息
        activitySkuMapper.delete((new LambdaQueryWrapper<ActivitySku>().
                eq(ActivitySku::getActivityId,activityId)));
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        //activityRuleList从前端传回的数据只有满减类型，甚至没有activityId
        ActivityInfo activityInfo = baseMapper.selectById(activityId);
        for (ActivityRule activityRule:activityRuleList
             ) {
            activityRule.setActivityId(activityId);
            activityRule.setActivityType(activityInfo.getActivityType());
            activityRuleMapper.insert(activityRule);
        }
        //保存sku信息
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        for (ActivitySku activitySku :activitySkuList) {
            activitySku.setActivityId(activityId);
            activitySkuMapper.insert(activitySku);
        }
    }
    //关键字查询
    @Override
    public Object findSkuInfoByKeyword(String keyword) {
        //远程调用service-product获得skuInfo
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        if(skuInfoList.size()==0){
            return skuInfoList;
        }
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        //防止商品参加两种及以上的满减优惠活动
        //查询出正在参加的商品id
        List<Long> existSkuIdList = baseMapper.selectSkuIdListExist(skuIdList);
        //排除
        List<SkuInfo> findSkuIdList=new ArrayList<>();
        for (SkuInfo skuInfo:skuInfoList
             ) {
            if (!existSkuIdList.contains(skuInfo.getId())){
                findSkuIdList.add(skuInfo);
            }
        }
        return findSkuIdList;
    }

    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result=new HashMap<>();
        for (Long skuId: skuIdList
             ) {
           List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
           if(CollectionUtils.isEmpty(activityRuleList)){
               List<String> RuleDescList = activityRuleList.stream()
                                            .map(activityRule -> activityRule.getRuleDesc())
                                            .collect(Collectors.toList());
               result.put(skuId,RuleDescList);
           }
        }

        return result;
    }

    @Override
    public OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId) {
        //
        //List<CartInfo> collect = cartInfoList.stream().filter(cartInfo -> cartInfo.getIsChecked() == 1).collect(Collectors.toList());

        List<CartInfoVo> carInfoVoList = this.findCartActivityList(cartInfoList);
        //促销活动优惠的总金额
        BigDecimal activityReduceAmount = carInfoVoList.stream()
                .filter(carInfoVo -> null != carInfoVo.getActivityRule())
                .map(carInfoVo -> carInfoVo.getActivityRule().getReduceAmount())
                //reduce求和流（初始值,相加字段）
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //购物车可使用的优惠券列表
        List<CouponInfo> couponInfoList = couponInfoService.findCartCouponInfo(cartInfoList, userId);
        //优惠券可优惠的总金额，一次购物只能使用一张优惠券
        BigDecimal couponReduceAmount = new BigDecimal(0);
        if(!CollectionUtils.isEmpty(couponInfoList)) {
            couponReduceAmount = couponInfoList.stream()
                    .filter(couponInfo -> couponInfo.getIsOptimal().intValue() == 1)
                    .map(couponInfo -> couponInfo.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

//        //购物车总金额
//        BigDecimal carInfoTotalAmount = cartInfoList.stream()
//                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
//                .map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //购物车原始总金额
        BigDecimal originalTotalAmount = cartInfoList.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //最终总金额
        BigDecimal totalAmount = originalTotalAmount.subtract(activityReduceAmount).subtract(couponReduceAmount);

        OrderConfirmVo orderTradeVo = new OrderConfirmVo();
        orderTradeVo.setCarInfoVoList(carInfoVoList);
        orderTradeVo.setActivityReduceAmount(activityReduceAmount);
        orderTradeVo.setCouponInfoList(couponInfoList);
        orderTradeVo.setCouponReduceAmount(couponReduceAmount);
        orderTradeVo.setOriginalTotalAmount(originalTotalAmount);
        orderTradeVo.setTotalAmount(totalAmount);
        return orderTradeVo;
    }

    @Override
    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList) {
        //根据活动规则对商品进行分组
        List<CartInfoVo> cartInfoVoList=new ArrayList<>();
        //获得每个商品的skuId---》active_sku--->activityId
        List<Long> skuIdList = cartInfoList.stream().map(CartInfo::getSkuId)
                                                .collect(Collectors.toList());
        List<ActivitySku> activitySkuList= baseMapper.findCartActivity(skuIdList);
        //根据活动Id进行分组，每个activityId---》（skuId，skuId）
        //key--activityId value--Set(skuId)
        Map<Long, Set<Long>> activityIdToSkuIdListMap = activitySkuList.stream().collect(
                Collectors.groupingBy(ActivitySku::getActivityId,
                        Collectors.mapping(ActivitySku::getSkuId, Collectors.toSet())
                ));
        //获得活动对应的规则
        Map<Long,List<ActivityRule>> activityToActivityRuleListMap=new HashMap<>();
        Set<Long> activityIdList = activitySkuList.stream().map(ActivitySku::getActivityId)
                                                            .collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(activityIdList)){
            LambdaQueryWrapper<ActivityRule> wrapper=new LambdaQueryWrapper<>();
            wrapper.in(ActivityRule::getActivityId,activityIdList);
            wrapper.orderByDesc(ActivityRule::getConditionAmount,ActivityRule::getConditionNum);
            List<ActivityRule> activityRuleList = activityRuleMapper.selectList(wrapper);
            //activityToActivityRuleListMap.put()
            activityToActivityRuleListMap = activityRuleList.stream().collect(
                    Collectors.groupingBy(activityRule ->activityRule.getActivityId()));
        }
        Set<Long> activitySkuIdSet = new HashSet<>();
        //计算活动商品的最优解
        if(!CollectionUtils.isEmpty(activityIdToSkuIdListMap)){
            Iterator<Map.Entry<Long, Set<Long>>> iterator = activityIdToSkuIdListMap
                                                            .entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Long, Set<Long>> entry = iterator.next();
                Long activityId= entry.getKey();
                Set<Long> currentActivitySkuIdSet= entry.getValue();
                //获得参数当前活动的购物车列表数据 ,使用过滤器
                List<CartInfo> currentActivityCartInfoList = cartInfoList.stream().filter(cartInfo ->
                        currentActivitySkuIdSet.contains(cartInfo.getSkuId()))
                        .collect(Collectors.toList());
                //得到在当前一个活动类型中购物车的总金额和总数量
                BigDecimal activityTotalAmount = this.computeTotalAmount(currentActivityCartInfoList);
                Integer activityTotalNum = this.computeCartNum(currentActivityCartInfoList);
                //计算当前活动对应的商品，每一个活动只能是一个类型 满减或者满量
                //得到当前活动的规则
                List<ActivityRule> currentActivityRuleList = activityToActivityRuleListMap.get(activityId);
                //得到活动类型 每个活动只能有一个类型
                ActivityType activityType = currentActivityRuleList.get(0).getActivityType();
                //判断活动类型 activityRule当前规则包含优惠后的金额
                ActivityRule activityRule=null;
                if(activityType==ActivityType.FULL_REDUCTION){
                     activityRule = this.computeFullReduction(activityTotalAmount, currentActivityRuleList);
                }else{
                     activityRule = this.computeFullDiscount(
                                        activityTotalNum,activityTotalAmount,currentActivityRuleList);
                }
                // //同一活动对应的购物项列表与对应优化规则
                CartInfoVo cartInfoVo = new CartInfoVo();
                //当前活动规则中购物车
                cartInfoVo.setCartInfoList(currentActivityCartInfoList);
                //参加活动规则后的最优惠解
                cartInfoVo.setActivityRule(activityRule);
                cartInfoVoList.add(cartInfoVo);
                //记录哪些商品参加了优惠活动 +当前活动的所有商品Id
                activitySkuIdSet.addAll(currentActivitySkuIdSet);
            }
        }
        //移除参加活动Id
        skuIdList.removeAll(activitySkuIdSet);
        if(!CollectionUtils.isEmpty(skuIdList)) {
            //获取skuId对应的购物项
            Map<Long, CartInfo> skuIdToCartInfoMap = cartInfoList.stream().collect(Collectors.toMap(CartInfo::getSkuId, CartInfo->CartInfo));
            CartInfoVo carInfoVo = new CartInfoVo();
            carInfoVo.setActivityRule(null);
            List<CartInfo> currentCartInfoList = new ArrayList<>();
            for(Long skuId : skuIdList) {
                currentCartInfoList.add(skuIdToCartInfoMap.get(skuId));
            }
            //封装无活动下的所有cartInfo信息
            carInfoVo.setCartInfoList(currentCartInfoList);
            //将无规则下的情况放入
            cartInfoVoList.add(carInfoVo);
        }
        return cartInfoVoList;
    }


    /**
     * 计算满量打折最优规则
     * @param totalNum
     * @param activityRuleList //该活动规则skuActivityRuleList数据，已经按照优惠折扣从大到小排序了
     */
    private ActivityRule computeFullDiscount(Integer totalNum, BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项购买个数大于等于满减件数，则优化打折
            if (totalNum.intValue() >= activityRule.getConditionNum()) {
                BigDecimal skuDiscountTotalAmount = totalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                BigDecimal reduceAmount = totalAmount.subtract(skuDiscountTotalAmount);
                activityRule.setReduceAmount(reduceAmount);
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，还差")
                    .append(totalNum-optimalActivityRule.getConditionNum())
                    .append("件");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("元打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }

    /**
     * 计算满减最优规则
     * @param totalAmount
     * @param activityRuleList //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
     */
    private ActivityRule computeFullReduction(BigDecimal totalAmount, List<ActivityRule> activityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动规则skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : activityRuleList) {
            //如果订单项金额大于等于满减金额，则优惠金额
            if (totalAmount.compareTo(activityRule.getConditionAmount()) > -1) {
                //优惠后减少金额
                activityRule.setReduceAmount(activityRule.getBenefitAmount());
                optimalActivityRule = activityRule;
                break;
            }
        }
        if(null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = activityRuleList.get(activityRuleList.size()-1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，还差")
                    .append(totalAmount.subtract(optimalActivityRule.getConditionAmount()))
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
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

    private int computeCartNum(List<CartInfo> cartInfoList) {
        int total = 0;
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                total += cartInfo.getSkuNum();
            }
        }
        return total;
    }
}
