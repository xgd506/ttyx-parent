package hue.xgd.ttyx.product.controller;


import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.product.Attr;
import hue.xgd.ttyx.product.service.AttrService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品属性 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@RestController
@RequestMapping("/admin/product/attr")
@CrossOrigin
public class AttrController {
    @Resource
    private AttrService attrService;

    @GetMapping("{groupId}")
    public Result getList(@PathVariable Long groupId){
        List<Attr> attrList= attrService.selectByGroupId(groupId);
        return Result.ok(attrList);
    }
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Attr attr = attrService.getById(id);
        return Result.ok(attr);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Attr attr) {
        attrService.save(attr);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Attr attr) {
        attrService.updateById(attr);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        attrService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrService.removeByIds(idList);
        return Result.ok(null);
    }
}

