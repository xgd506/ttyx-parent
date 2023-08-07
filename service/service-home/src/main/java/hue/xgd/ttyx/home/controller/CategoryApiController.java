package hue.xgd.ttyx.home.controller;

import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:xgd
 * @Date:2023/8/5 18:15
 * @Description:
 */
@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {


    @Resource
    private ProductFeignClient productFeignClient;
    //查询所有分类
    @ApiOperation(value = "获取分类信息")
    @GetMapping("category")
    public Result index() {
        return Result.ok(productFeignClient.findAllCategoryList());
    }

}
