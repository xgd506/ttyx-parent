package hue.xgd.ttyx.sys.controller;


import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.sys.service.RegionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@RestController
@RequestMapping("/admin/sys/region")
@CrossOrigin
public class RegionController {
    @Resource
    private RegionService regionService;

    @ApiOperation(value = "根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return Result.ok(regionService.findRegionByKeyword(keyword));
    }
}

