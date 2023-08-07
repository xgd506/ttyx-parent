package hue.xgd.ttyx.product.api;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.product.service.CategoryService;
import hue.xgd.ttyx.product.service.SkuInfoService;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:10
 * @Description:
 */
@RestController
@RequestMapping("/api/product")
public class ProductInnerController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private SkuInfoService skuInfoService;

    //根据skuid获取分类信息
    @ApiOperation(value = "根据分类id获取分类信息")
    @GetMapping("inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }
    //根据skuid获取sku信息
    @ApiOperation(value = "根据skuId获取sku信息")
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        return skuInfoService.getById(skuId);
    }

    //根据skuid列表活动skuInfo信息
    @PostMapping("inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList){
        return skuInfoService.findSkuInfoList(skuIdList);
    }

    @ApiOperation(value = "根据关键字获取sku列表")
    @GetMapping("inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return skuInfoService.findSkuInfoByKeyword(keyword);
    }

    @PostMapping("inner/findCategoryList")
    public List<Category> findCategoryList(@RequestBody List<Long> categoryList){
        return categoryService.listByIds(categoryList);
    }

    @ApiOperation(value = "获取分类信息")
    @GetMapping("inner/findAllCategoryList")
    public List<Category> findAllCategoryList() {
        return categoryService.findAllList();
    }

    @ApiOperation(value = "获取新人专享")
    @GetMapping("inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList() {
        return skuInfoService.findNewPersonList();
    }

    @ApiOperation(value = "获取Sku信息")
    @GetMapping("inner/get/{id}")
    public SkuInfoVo get(@PathVariable Long id){
        return skuInfoService.getSkuInfoVo(id);
    }
}
