package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.AdminRoleMapper;
import hue.xgd.ttyx.acl.service.AdminRoleService;
import hue.xgd.ttyx.model.acl.AdminRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/1 17:33
 * @Description:
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {

    @Override
    public List<AdminRole> List(LambdaQueryWrapper<AdminRole> wrapper) {
        List<AdminRole> list = baseMapper.selectList(wrapper);
        return list;
    }
}
