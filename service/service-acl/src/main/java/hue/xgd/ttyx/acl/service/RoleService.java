package hue.xgd.ttyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.acl.Role;
import hue.xgd.ttyx.vo.acl.RoleQueryVo;

import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/1 15:24
 * @Description:
 */
public interface RoleService extends IService<Role> {

    IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    Map<String, Object> getRoleIdByAdminId(Long adminId);

    void saveAdminRole(Long adminId, Long[] roleId);
}
