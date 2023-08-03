package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.AttrGroup;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.product.mapper.AttrGroupMapper;
import hue.xgd.ttyx.product.service.AttrGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.vo.product.AttrGroupQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {

    @Override
    public IPage<AttrGroup> selectCategory(Page<AttrGroup> pageParams, AttrGroupQueryVo attrGroupQueryVo) {
        String name = attrGroupQueryVo.getName();
        LambdaQueryWrapper<AttrGroup> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.like(AttrGroup::getName,name);
        }
        Page<AttrGroup> attrGroupPage = baseMapper.selectPage(pageParams, wrapper);
        return attrGroupPage;
    }

    @Override
    public List<AttrGroup> findAllList() {
        //LambdaQueryWrapper<AttrGroup> wrapper=new LambdaQueryWrapper<>();
        //wrapper.orderByDesc(AttrGroup::getSort);
        QueryWrapper<AttrGroup> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<AttrGroup> attrGroupList = baseMapper.selectList(wrapper);
        //return this.list(wrapper);
        return attrGroupList;
    }
}
