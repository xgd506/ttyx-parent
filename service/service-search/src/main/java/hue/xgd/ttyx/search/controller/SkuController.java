package hue.xgd.ttyx.search.controller;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.search.service.SkuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/3 15:55
 * @Description:
 */
@RestController
@RequestMapping("/admin/search/sku")
public class SkuController {
    @Resource
    private SkuService service;
    //上架
    @GetMapping("inner/upperSku/{skuId}")
    public Result upperSku(@PathVariable Long skuId){
        service.upperSku(skuId);
        return Result.ok(null);

    }
    //下架
    @GetMapping("inner/lowerSku/{skuId}")
    public Result downSku(@PathVariable Long skuId){
        service.lowerSku(skuId);
        return Result.ok(null);
    }

    //删除
    @GetMapping("inner/deleteSku/{skuId}")
    public Result deleteSku(@PathVariable Long skuId){
        service.deleteSku(skuId);
        return Result.ok(null);
    }
    //批量删除 懒得创建队列了
    @GetMapping("inner/BatchDeleteSku/{skuId}")
    public Result BatchDeleteSku(@PathVariable List<Long> skuId){
        service.BatchdDleteSku(skuId);
        return Result.ok(null);
    }
}
