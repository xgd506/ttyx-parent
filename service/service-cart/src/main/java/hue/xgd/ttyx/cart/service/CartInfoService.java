package hue.xgd.ttyx.cart.service;

import hue.xgd.ttyx.model.order.CartInfo;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/7 15:26
 * @Description:
 */
public interface CartInfoService {
    void addToCart(Long userId, Long skuId, Integer skuNum);

    void deleteCart(Long skuId, Long userId);

    void deleteAllCart(Long userId);

    void batchDeleteCart(List<Long> skuIdList, Long userId);

    List<CartInfo> getCartList(Long userId);

    void checkCart(Long userId, Integer isChecked, Long skuId);

    void checkAllCart(Long userId, Integer isChecked);

    void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked);
}
