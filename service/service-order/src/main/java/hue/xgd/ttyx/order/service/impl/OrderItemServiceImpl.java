package hue.xgd.ttyx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.model.order.OrderItem;
import hue.xgd.ttyx.order.mapper.OrderItemMapper;
import hue.xgd.ttyx.order.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * @Author:xgd
 * @Date:2023/8/10 14:18
 * @Description:
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}
