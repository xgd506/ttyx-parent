package hue.xgd.ttyx.client.activity;

import hue.xgd.ttyx.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
