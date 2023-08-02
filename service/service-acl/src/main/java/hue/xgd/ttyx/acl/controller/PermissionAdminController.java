package hue.xgd.ttyx.acl.controller;

import hue.xgd.ttyx.acl.service.PermissionService;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.acl.Permission;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/2 9:57
 * @Description:
 */
@RestController
@RequestMapping("/admin/acl/permission")
@CrossOrigin
public class PermissionAdminController {
    @Resource
    private PermissionService permissionService;

// 获取权限(菜单/功能)列表
    @GetMapping("")
    public Result get(){
        List<Permission> list = permissionService.listWithTree();
        return Result.ok(list);
    }

    //  删除一个权限项
    @GetMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        permissionService.removeMenusById(id);
        return Result.ok(null);
    }
    // 保存一个权限项
    @PostMapping("save")
    public Result save(@RequestBody Permission permission){
        boolean b = permissionService.save(permission);
        if (b) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }
    //更新一个权限项
    @PutMapping("update")
    public Result update(@RequestBody Permission permission){
        boolean b = permissionService.updateById(permission);
        if (b) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }
    //  查看某个角色的权限列表
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        List<Permission> result= permissionService.getPermissionByRoleId(roleId);
        return Result.ok(result);
    }
    // 给某个角色授权
    @PostMapping("doAssign")
    public Result doAssign(@RequestParam Long roleId,
                           @RequestParam Long[] permissionId){
        permissionService.saveRolePermission(roleId,permissionId);
        return Result.ok(null);
    }
}
