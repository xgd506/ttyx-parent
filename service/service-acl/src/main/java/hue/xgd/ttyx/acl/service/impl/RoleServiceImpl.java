package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.AdminRoleMapper;
import hue.xgd.ttyx.acl.mapper.RoleMapper;
import hue.xgd.ttyx.acl.service.AdminRoleService;
import hue.xgd.ttyx.acl.service.RoleService;
import hue.xgd.ttyx.model.acl.AdminRole;
import hue.xgd.ttyx.model.acl.Role;
import hue.xgd.ttyx.vo.acl.RoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:xgd
 * @Date:2023/8/1 15:25
 * @Description:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {
    @Resource private AdminRoleService adminRoleService;
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
         //获得传递过来的角色值
        String roleName = roleQueryVo.getRoleName();
        //创建mq条件对象
        LambdaQueryWrapper<Role> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(roleName)){
            //模糊匹配 role_name like ?(%roleName%)
            wrapper.like(Role::getRoleName,roleName);
        }
        //调用方法实现条件查询
        Page<Role> page = baseMapper.selectPage(pageParam, wrapper);
        //返回查询结果
        return page;
    }

    @Override
    public Map<String, Object> getRoleIdByAdminId(Long adminId) {
        //1.获得所有角色
        List<Role> allRolesList = baseMapper.selectList(null);
        //2.根据用户id查询分配角色
        //2.1根据用户id查询
        LambdaQueryWrapper<AdminRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        List<AdminRole> allRoles = adminRoleService.List(wrapper);
        //2.2将用户的角色id保存到List<Long>中
        List<Long> assignRoleList = allRoles.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        //判断所有角色中是否包含分配的角色,并将该角色保存
        List<Role> assignRoles=new ArrayList<>();
        for (Role role:allRolesList) {
            if(assignRoleList.contains(role.getId())){
                //该角色是用户分配的角色
                assignRoles.add(role);
            }
        }
        
        //3.封装结果
        Map<String,Object> result =new HashMap<>();
        result.put("allRolesList",allRolesList);
        result.put("assignRoles",assignRoles);
        return result;
    }

    @Override
    public void saveAdminRole(Long adminId, Long[] roleId) {
        //1.删除之前用户的关系
        LambdaQueryWrapper<AdminRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId,adminId);
        adminRoleService.remove(wrapper);
        //2.遍历roleId，将用户id和角色id一起保存到关系表中
        List<AdminRole> list=new ArrayList<>();
        for (Long id:roleId){
            AdminRole adminRole=new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(id);
            list.add(adminRole);
        }
       adminRoleService.saveBatch(list);
    }
}
