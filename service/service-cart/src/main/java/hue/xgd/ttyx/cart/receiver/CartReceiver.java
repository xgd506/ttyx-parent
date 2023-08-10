package hue.xgd.ttyx.cart.receiver;

import com.rabbitmq.client.Channel;
import hue.xgd.ttyx.cart.service.CartInfoService;
import hue.xgd.ttyx.rabbit.constant.MqConst;
import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author:xgd
 * @Date:2023/8/10 14:45
 * @Description:
 */
@Component
public class CartReceiver {
    @Resource
    private CartInfoService cartInfoService;

    @RabbitListener( bindings = @QueueBinding(
            value =@Queue(value = MqConst.QUEUE_DELETE_CART,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_ORDER_DIRECT),
            key = {MqConst.ROUTING_DELETE_CART}
    ))
    public void deleteCart(Long userId, Message message, Channel channel) throws IOException {
        if(userId!=null){
            cartInfoService.deleteCartChecked(userId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
