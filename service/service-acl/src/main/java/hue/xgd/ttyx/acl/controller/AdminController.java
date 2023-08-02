package hue.xgd.ttyx.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.acl.service.AdminRoleService;
import hue.xgd.ttyx.acl.service.AdminService;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.acl.Admin;
import hue.xgd.ttyx.model.acl.AdminRole;
import hue.xgd.ttyx.util.MD5;
import hue.xgd.ttyx.vo.acl.AdminQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/1 17:04
 * @Description:
 */
@RestController
@RequestMapping("/admin/acl/user")
@CrossOrigin
public class AdminController {
    @Resource private AdminService adminService;
    @Resource private AdminRoleService adminRoleService;

    //1.获取后台用户分页列表(带搜索)
    @GetMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit,
                              AdminQueryVo adminQueryVo){
        Page<Admin> pageParams=new Page<>(page,limit);
        IPage<Admin> pageModel= adminService.selectAdminPage(pageParams,adminQueryVo);
        return Result.ok(pageModel);
    }
    //2.根据ID获取某个后台用户
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        Admin admin = adminService.getById(id);
        if(admin!=null){
            return Result.ok(admin);
        }else{
            return Result.fail(null);
        }
    }
    //3.保存一个新的后台用户
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin){
        String password = admin.getPassword();
        String MD5Password = MD5.encrypt(password);
        System.out.println(MD5Password);
        admin.setPassword(MD5Password);
        boolean b = adminService.save(admin);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //4.更新一个后台用户
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        boolean b = adminService.updateById(admin);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //5.获取某个用户的所有角色
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId){
       List<AdminRole> list= adminRoleService.getRoleIdByAdminId(adminId);
       return Result.ok(list);
    }
    //6.删除某个用户
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = adminService.removeById(id);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
    //批量删除多个用户
    //ids的结构: ids是包含n个id的数组
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean b =adminService.removeByIds(ids);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

}
