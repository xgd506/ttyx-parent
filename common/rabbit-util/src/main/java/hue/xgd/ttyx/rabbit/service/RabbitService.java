package hue.xgd.ttyx.rabbit.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:xgd
 * @Date:2023/8/3 17:08
 * @Description:
 */
@Service
public class RabbitService {
    @Resource
    private RabbitTemplate rabbitTemplate;
    //发送消息
    public boolean sendMessage(String exchange,String routingKey,Object message){
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
        return true;
    }
}
