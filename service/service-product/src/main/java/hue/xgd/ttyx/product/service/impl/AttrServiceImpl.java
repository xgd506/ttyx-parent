package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hue.xgd.ttyx.model.product.Attr;
import hue.xgd.ttyx.product.mapper.AttrMapper;
import hue.xgd.ttyx.product.service.AttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {

    @Override
    public List<Attr> selectByGroupId(Long groupId) {
        LambdaQueryWrapper<Attr> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getAttrGroupId,groupId);
        List<Attr> attrs = baseMapper.selectList(wrapper);
        return attrs;
    }
}
