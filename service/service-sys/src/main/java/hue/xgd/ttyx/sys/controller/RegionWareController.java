package hue.xgd.ttyx.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.acl.Admin;
import hue.xgd.ttyx.model.sys.RegionWare;
import hue.xgd.ttyx.sys.service.RegionWareService;
import hue.xgd.ttyx.vo.acl.AdminQueryVo;
import hue.xgd.ttyx.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 城市仓库关联表 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@RestController
@RequestMapping("/admin/sys/regionWare")
@CrossOrigin
public class RegionWareController {
    @Resource
    private RegionWareService regionWareService;
    @GetMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit,
                             RegionWareQueryVo regionWareQueryVo) {
        Page<RegionWare> pageParams = new Page<>(page, limit);
        IPage<RegionWare> pageModel = regionWareService.selectAdminPage(pageParams, regionWareQueryVo);
        return Result.ok(pageModel);
    }

    //添加开通区域
    @PostMapping("save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.saveRegionWare(regionWare);
        return Result.ok(null);
    }

    //删除开通区域
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        regionWareService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "取消开通区域")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status) {
        regionWareService.updateStatus(id, status);
        return Result.ok(null);
    }
}

