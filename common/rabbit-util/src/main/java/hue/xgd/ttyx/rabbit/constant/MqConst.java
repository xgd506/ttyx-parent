package hue.xgd.ttyx.rabbit.constant;

/**
 * @Author:xgd
 * @Date:2023/8/3 17:16
 * @Description:
 */
public class MqConst {
    /**
     * 消息补偿
     */
    public static final String MQ_KEY_PREFIX = "ttyx.mq:list";
    public static final int RETRY_COUNT = 3;

    /**
     * 商品上下架
     */
    public static final String EXCHANGE_GOODS_DIRECT = "ttyx.goods.direct";
    public static final String ROUTING_GOODS_UPPER = "ttyx.goods.upper";
    public static final String ROUTING_GOODS_LOWER = "ttyx.goods.lower";
    public static final String ROUTING_GOODS_DELETE = "ttyx.goods.delete";
    //队列
    public static final String QUEUE_GOODS_UPPER  = "ttyx.goods.upper";
    public static final String QUEUE_GOODS_LOWER  = "ttyx.goods.lower";
    public static final String QUEUE_GOODS_DELETE  = "ttyx.goods.delete";


    /**
     * 团长上下线
     */
    public static final String EXCHANGE_LEADER_DIRECT = "ttyx.leader.direct";
    public static final String ROUTING_LEADER_UPPER = "ttyx.leader.upper";
    public static final String ROUTING_LEADER_LOWER = "ttyx.leader.lower";
    //队列
    public static final String QUEUE_LEADER_UPPER  = "ttyx.leader.upper";
    public static final String QUEUE_LEADER_LOWER  = "ttyx.leader.lower";

    //订单
    public static final String EXCHANGE_ORDER_DIRECT = "ttyx.order.direct";
    public static final String ROUTING_ROLLBACK_STOCK = "ttyx.rollback.stock";
    public static final String ROUTING_MINUS_STOCK = "ttyx.minus.stock";

    public static final String ROUTING_DELETE_CART = "ttyx.delete.cart";
    //解锁普通商品库存
    public static final String QUEUE_ROLLBACK_STOCK = "ttyx.rollback.stock";
    public static final String QUEUE_SECKILL_ROLLBACK_STOCK = "ttyx.seckill.rollback.stock";
    public static final String QUEUE_MINUS_STOCK = "ttyx.minus.stock";
    public static final String QUEUE_DELETE_CART = "ttyx.delete.cart";

    //支付
    public static final String EXCHANGE_PAY_DIRECT = "ttyx.pay.direct";
    public static final String ROUTING_PAY_SUCCESS = "ttyx.pay.success";
    public static final String QUEUE_ORDER_PAY  = "ttyx.order.pay";
    public static final String QUEUE_LEADER_BILL  = "ttyx.leader.bill";

    //取消订单
    public static final String EXCHANGE_CANCEL_ORDER_DIRECT = "ttyx.cancel.order.direct";
    public static final String ROUTING_CANCEL_ORDER = "ttyx.cancel.order";
    //延迟取消订单队列
    public static final String QUEUE_CANCEL_ORDER  = "ttyx.cancel.order";

    /**
     * 定时任务
     */
    public static final String EXCHANGE_DIRECT_TASK = "ttyx.exchange.direct.task";
    public static final String ROUTING_TASK_23 = "ttyx.task.23";
    //队列
    public static final String QUEUE_TASK_23  = "ttyx.queue.task.23";
}
