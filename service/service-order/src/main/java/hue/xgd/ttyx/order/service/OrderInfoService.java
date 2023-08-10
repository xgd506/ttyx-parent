package hue.xgd.ttyx.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.order.OrderInfo;
import hue.xgd.ttyx.vo.order.OrderConfirmVo;
import hue.xgd.ttyx.vo.order.OrderSubmitVo;
import hue.xgd.ttyx.vo.order.OrderUserQueryVo;

/**
 * @Author:xgd
 * @Date:2023/8/9 13:22
 * @Description:
 */
public interface OrderInfoService extends IService<OrderInfo> {
    OrderConfirmVo confirmOrder();

    Long submitOrder(OrderSubmitVo orderParamVo);

    OrderInfo getOrderInfoById(Long orderId);

    void orderPay(String orderNo);

    OrderInfo getOrderInfoByOrderNo(String orderNo);

    IPage<OrderInfo> findUserOrderPage(Page<OrderInfo> pageParam, OrderUserQueryVo orderUserQueryVo);
}
