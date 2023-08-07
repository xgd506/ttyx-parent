package hue.xgd.ttyx.activity.api;

import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.activity.service.CouponInfoService;
import hue.xgd.ttyx.model.activity.CouponInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/5 19:18
 * @Description:
 */
@RestController
@RequestMapping("api/activity")
public class ActivityApiController {
    @Resource
    private ActivityInfoService activityInfoService;
    @Resource
    private CouponInfoService couponInfoService;

    @PostMapping("inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList){
        return  activityInfoService.findActivity(skuIdList);
    }

    @GetMapping("inner/findCoupon/{skuId}/{userId}")
    public Map<String,Object> findCoupon(@PathVariable("skuId") Long skuId,
                                         @PathVariable("userId") Long userId){
          return  couponInfoService.getCouponInfoByUserId(skuId,userId);
    }

}
