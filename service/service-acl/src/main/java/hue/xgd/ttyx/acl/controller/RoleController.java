package hue.xgd.ttyx.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.acl.service.RoleService;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.acl.Role;
import hue.xgd.ttyx.vo.acl.RoleQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/1 15:21
 * @Description:
 * 在前端的HTTP请求中，params和data都是用来传递数据给后端的，但它们在传递数据的方式上有一些区别。
 * params：
 * params通常用于HTTP GET请求和部分HTTP POST请求。
 * 在HTTP GET请求中，params用于将数据附加在URL的查询字符串中，形成类似?key1=value1&key2=value2的形式。
 * 在部分HTTP POST请求中，例如application/x-www-form-urlencoded格式的POST请求，params也用于将数据编码成URL查询字符串，并放在请求体中发送给后端。
 * 适用于简单的键值对数据传递，不适合传递复杂的结构化数据。
 *
 * data：
 * data通常用于HTTP POST请求，特别是用于传递结构化的数据，例如JSON格式的数据。
 * 在HTTP POST请求中，data用于将数据放在请求体中，而不是附加在URL中。这种方式更适合传递大量、复杂的数据。
 * 适用于传递复杂结构的数据，例如JSON对象、数组等。
 */
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin
public class RoleController {

    @Resource private RoleService roleService;

    //1.角色的列表  条件分页查询  前端是params作为url参数通过映射传递值
    @GetMapping("{current}/{limit}")
    public Result pageList(@PathVariable("current") Long current,
                           @PathVariable("limit") Long limit,
                           RoleQueryVo roleQueryVo){
    //创建page对象
        Page<Role> pageParam=new Page<>(current,limit);
    //实现分页
        IPage<Role> pageModel= roleService.selectRolePage(pageParam,roleQueryVo);
        return Result.ok(pageModel);
    }
    //2.根据id查询角色
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
    //3.添加角色  前端传来的是data 以请求体也就是json格式传输 用@RequestBody接收
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        boolean b = roleService.save(role);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }

    }
    //4.修改角色
    @PutMapping("update")
    public Result update(@RequestBody Role role){
        boolean b = roleService.updateById(role);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //删除角色通过id
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = roleService.removeById(id);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //批量删除 json的数组格式对于List
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean b = roleService.removeByIds(ids);
        if(b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }
}
