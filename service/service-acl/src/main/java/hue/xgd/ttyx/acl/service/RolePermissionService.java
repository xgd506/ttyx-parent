package hue.xgd.ttyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.acl.RolePermission;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/2 10:47
 * @Description:
 */
public interface RolePermissionService extends IService<RolePermission> {
    List<RolePermission> List(Long roleId);
}
