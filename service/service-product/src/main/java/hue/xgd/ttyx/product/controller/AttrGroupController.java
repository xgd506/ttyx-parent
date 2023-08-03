package hue.xgd.ttyx.product.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.product.AttrGroup;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.product.service.AttrGroupService;
import hue.xgd.ttyx.vo.product.AttrGroupQueryVo;
import hue.xgd.ttyx.vo.product.CategoryQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 属性分组 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Api(value = "AttrGroup管理", tags = "平台属性分组管理")
@RestController
@RequestMapping("/admin/product/attrGroup")
@CrossOrigin
public class AttrGroupController {
    @Resource
    private AttrGroupService attrGroupService;
    @GetMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit,
                             AttrGroupQueryVo attrGroupQueryVo){
        Page<AttrGroup> pageParams=new Page<>(page,limit);
        IPage<AttrGroup> result= attrGroupService.selectCategory(pageParams,attrGroupQueryVo);
        return Result.ok(result);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        AttrGroup attrGroup = attrGroupService.getById(id);
        return Result.ok(attrGroup);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody AttrGroup attrGroup) {
        attrGroupService.save(attrGroup);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody AttrGroup attrGroup) {
        attrGroupService.updateById(attrGroup);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        attrGroupService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        attrGroupService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation(value = "查询所有分组")
    @GetMapping("findAllList")
    public Result findAllList(){
        List<AttrGroup> attrGroupList= attrGroupService.findAllList();
        return Result.ok(attrGroupList);
    }
}

