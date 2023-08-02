package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.PermissionMapper;
import hue.xgd.ttyx.acl.service.PermissionService;
import hue.xgd.ttyx.acl.service.RolePermissionService;
import hue.xgd.ttyx.common.exception.TtyxException;
import hue.xgd.ttyx.model.acl.Permission;
import hue.xgd.ttyx.model.acl.RolePermission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:xgd
 * @Date:2023/8/2 10:00
 * @Description:
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public List<Permission> listWithTree() {
        //查询所有分类
        List<Permission> permissionList = baseMapper.selectList(null);
        //组成父子的树形结构
        List<Permission> collect = permissionList.stream().filter(permission -> permission.getPid() == 0)
                .map((menus) -> {
                    menus.setChildren(getChildren(menus, permissionList));
                    return menus;
                }).collect(Collectors.toList());
        return collect;
    }

    private List<Permission> getChildren(Permission root, List<Permission> permissionList) {
        List<Permission> children = permissionList.stream().filter(permission -> {
            return permission.getPid() == root.getId();
        }).map(permission -> {
            permission.setChildren(getChildren(permission, permissionList));
            return permission;
        }).collect(Collectors.toList());
        return children;

    }
    //删除菜单
    @Override
    public void removeMenusById(Long id) {
        LambdaQueryWrapper<Permission> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid,id);
        Integer integer = baseMapper.selectCount(wrapper);
        if(integer>0){
            throw new TtyxException(201,"菜单不能删除");
        }
        baseMapper.deleteById(id);

    }

    @Override
    public void saveRolePermission(Long roleId, Long[] permissionId) {
        //删除原来的表中关系
        LambdaQueryWrapper<RolePermission> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        rolePermissionService.remove(wrapper);
        List<RolePermission> list=new ArrayList<>();
        //遍历permissionId
        for (Long id:permissionId) {
            RolePermission rolePermission=new RolePermission();
            rolePermission.setPermissionId(id);
            rolePermission.setRoleId(roleId);
            list.add(rolePermission);
        }
        rolePermissionService.saveBatch(list);
    }

    //查询角色权限
    @Override
    public List<Permission> getPermissionByRoleId(Long roleId) {
        //查询所有权限
        List<Permission> permissionList = listWithTree();
        //查询当前角色所有权限Id
        List<RolePermission> RolePermissionList= rolePermissionService.List(roleId);
        List<Long> collect = RolePermissionList.stream().map(item -> item.getPermissionId()).
                collect(Collectors.toList());


        permissionList.stream().forEach(permission -> {
            if(collect.contains(permission.getId())){
                permission.setSelect(true);
            }else {
                permission.setSelect(false);
            }
        });
        //封装结果集
        return permissionList;
    }


}
