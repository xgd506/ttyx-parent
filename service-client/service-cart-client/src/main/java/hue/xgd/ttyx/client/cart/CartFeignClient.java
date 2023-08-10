package hue.xgd.ttyx.client.cart;

import hue.xgd.ttyx.model.order.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/9 13:39
 * @Description:
 */
@FeignClient("service-order")
public interface CartFeignClient {

    @GetMapping("inner/getCartCheckedList/{userId}")
     List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId);
}
