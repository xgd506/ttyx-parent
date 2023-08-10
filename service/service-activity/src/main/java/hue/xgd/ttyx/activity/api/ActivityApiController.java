package hue.xgd.ttyx.activity.api;

import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.activity.service.CouponInfoService;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.order.CartInfo;
import hue.xgd.ttyx.vo.order.CartInfoVo;
import hue.xgd.ttyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @ApiOperation(value = "获取购物车满足条件的促销与优惠券信息")
    @PostMapping("inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId) {
        return activityInfoService.findCartActivityAndCoupon(cartInfoList, userId);
    }
     @PostMapping("inner/findCartActivityList")
     public  List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList){
        return activityInfoService.findCartActivityList(cartInfoList);
     }
     @PostMapping("inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList,
                                         @PathVariable("couponId") Long couponId){
        return couponInfoService.findRangeSkuIdList(cartInfoList,couponId);
     }

     @GetMapping("inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId,
                                             @PathVariable("userId") Long userId,
                                             @PathVariable("orderId") Long orderId){
       couponInfoService.updateCouponInfoUseStatus(couponId,userId,orderId);
       return true;
     }
}
