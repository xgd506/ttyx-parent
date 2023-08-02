package hue.xgd.ttyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.acl.Permission;

import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/2 9:59
 * @Description:
 */
public interface PermissionService extends IService<Permission> {
    List<Permission> listWithTree();

    List<Permission> getPermissionByRoleId(Long roleId);

    void removeMenusById(Long id);

    void saveRolePermission(Long roleId, Long[] permissionId);
}
