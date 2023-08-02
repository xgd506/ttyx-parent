package hue.xgd.ttyx.product.service.impl;

import hue.xgd.ttyx.model.base.MqRepeatRecord;
import hue.xgd.ttyx.product.mapper.MqRepeatRecordMapper;
import hue.xgd.ttyx.product.service.MqRepeatRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * mq去重表 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class MqRepeatRecordServiceImpl extends ServiceImpl<MqRepeatRecordMapper, MqRepeatRecord> implements MqRepeatRecordService {

}
