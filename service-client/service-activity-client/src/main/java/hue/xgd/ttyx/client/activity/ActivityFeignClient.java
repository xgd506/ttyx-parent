package hue.xgd.ttyx.client.activity;

import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.order.CartInfo;
import hue.xgd.ttyx.vo.order.CartInfoVo;
import hue.xgd.ttyx.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/5 19:57
 * @Description:
 */
@FeignClient("service-activity")
public interface ActivityFeignClient {
    @PostMapping("/api/activity/inner/findActivity")
    Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);
    @GetMapping("/api/activity/inner/findCoupon/{skuId}/{userId}")
    Map<String,Object> findCoupon(@PathVariable("skuId") Long skuId,
                                        @PathVariable("userId") Long userId);
    @PostMapping("/api/activity/inner/findCartActivityAndCoupon/{userId}")
    OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList,
                                                    @PathVariable("userId") Long userId);

    @PostMapping("/api/activity/inner/findCartActivityList")
    List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList);

    @PostMapping("/api/activity/inner/findRangeSkuIdList/{couponId}")
    CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList,
                                         @PathVariable("couponId") Long couponId);

    @GetMapping("/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId,
                                             @PathVariable("userId") Long userId,
                                             @PathVariable("orderId") Long orderId);
}
