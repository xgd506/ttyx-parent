package hue.xgd.ttyx.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.product.AttrGroup;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.product.service.SkuInfoService;
import hue.xgd.ttyx.vo.product.AttrGroupQueryVo;
import hue.xgd.ttyx.vo.product.SkuInfoQueryVo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@RestController
@RequestMapping("/admin/product/skuInfo")
@CrossOrigin
public class SkuInfoController {
    @Resource
    private SkuInfoService skuInfoService;
    @GetMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit,
                             SkuInfoQueryVo skuInfoQueryVo){
        Page<SkuInfo> pageParams=new Page<>(page,limit);
        IPage<SkuInfo> result= skuInfoService.selectCategory(pageParams,skuInfoQueryVo);
        return Result.ok(result);
    }
    @ApiOperation(value = "获取Sku信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        SkuInfoVo skuInfoVo = skuInfoService.getSkuInfoVo(id);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation(value = "新增Sku")
    @PostMapping("save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改商品分类")
    @PutMapping("update")
    public Result updateById(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除sku")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
       skuInfoService.removeSkuInfo(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除sku")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeBatchSkuInfo(idList);
        return Result.ok(null);
    }

    @ApiOperation(value = "商品上架")
    @GetMapping("publish/{id}/{status}")
    public Result publish(@PathVariable Long id,
                          @PathVariable Integer status){
        if(status==1){
            //上架
            SkuInfo skuInfo = skuInfoService.getById(id);
            skuInfo.setPublishStatus(status);
            skuInfoService.updateById(skuInfo);
            //TODO  整合mq将消息保存到es
        }else{
            //下架
            SkuInfo skuInfo = skuInfoService.getById(id);
            skuInfo.setPublishStatus(status);
            skuInfoService.updateById(skuInfo);
            //TODO  整合mq将消息同步到es
        }
        return Result.ok(null);
    }
    @ApiOperation(value = "商品审核")
    @GetMapping("check/{id}/{status}")
    public Result check(@PathVariable Long id,
                          @PathVariable Integer status){
        SkuInfo skuInfo = skuInfoService.getById(id);
        skuInfo.setCheckStatus(status);
        skuInfoService.updateById(skuInfo);
        return Result.ok(null);
    }

    @ApiOperation(value = "新人专享")
    @GetMapping("isNewPerson/{id}/{status}")
    public Result isNewPerson(@PathVariable Long id,
                        @PathVariable Integer status){
        SkuInfo skuInfo = skuInfoService.getById(id);
        skuInfo.setIsNewPerson(status);
        skuInfoService.updateById(skuInfo);
        return Result.ok(null);
    }

}

