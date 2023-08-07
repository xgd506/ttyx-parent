package hue.xgd.ttyx.home.controller;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.common.security.AuthContextHolder;
import hue.xgd.ttyx.home.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/7 10:29
 * @Description:
 */
@Api(tags = "商品详情")
@RestController
@RequestMapping("api/home")
public class ItemApiController {

    @Resource
    private ItemService itemService;

    @ApiOperation(value = "获取sku详细信息")
    @GetMapping("item/{id}")
    public Result index(@PathVariable Long id, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> result=itemService.item(id, userId);
        return Result.ok(result);
    }

}