package hue.xgd.ttyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.activity.mapper.ActivityRuleMapper;
import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.activity.mapper.ActivityInfoMapper;
import hue.xgd.ttyx.activity.mapper.ActivitySkuMapper;
import hue.xgd.ttyx.model.activity.ActivityInfo;
import hue.xgd.ttyx.model.activity.ActivityRule;
import hue.xgd.ttyx.model.activity.ActivitySku;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.vo.activity.ActivityRuleVo;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
