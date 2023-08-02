package hue.xgd.ttyx.acl.controller;

import hue.xgd.ttyx.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/1 14:48
 * @Description:
 */
@RestController
@RequestMapping("/admin/acl/index")
@CrossOrigin //允许跨域访问
public class IndexController {

    //1登录
    @PostMapping("login")
    public Result login(){
        //返回token
        Map<String,String> map=new HashMap<>();
        map.put("username","admin");
        map.put("password","123456");
        return Result.ok(map);
    }
    //2登录信息
    @GetMapping("info")
    public Result info(){
        //返回token
        Map<String,String> map=new HashMap<>();
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }
    /**
     * 3 退出
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok(null );
    }
}
