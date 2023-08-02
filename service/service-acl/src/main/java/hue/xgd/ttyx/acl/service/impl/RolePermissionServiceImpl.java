package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.RolePermissionMapper;
import hue.xgd.ttyx.acl.service.RolePermissionService;
import hue.xgd.ttyx.model.acl.RolePermission;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/2 10:48
 * @Description:
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
                                        implements RolePermissionService {
    @Override
    public List<RolePermission> List(Long roleId) {
        LambdaQueryWrapper<RolePermission> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        List<RolePermission> rolePermissions = baseMapper.selectList(wrapper);
        return rolePermissions;
    }
}
