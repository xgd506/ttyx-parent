package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hue.xgd.ttyx.model.product.SkuAttrValue;
import hue.xgd.ttyx.product.mapper.SkuAttrValueMapper;
import hue.xgd.ttyx.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {

    @Override
    public List<SkuAttrValue> findBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuAttrValue> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId,id);
        List<SkuAttrValue> valueList = baseMapper.selectList(wrapper);
        return valueList;
    }

    @Override
    public void removeBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuAttrValue> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId,id);
        baseMapper.delete(wrapper);
    }

    @Override
    public void removeBatchSkuInfo(List<Long> idList) {
        for (Long skuId:idList
             ) {
            LambdaQueryWrapper<SkuAttrValue> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(SkuAttrValue::getSkuId,skuId);
            baseMapper.delete(wrapper);
        }
    }
}
