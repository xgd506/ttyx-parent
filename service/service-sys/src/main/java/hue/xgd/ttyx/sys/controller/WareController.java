package hue.xgd.ttyx.sys.controller;


import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.sys.service.WareService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 仓库表 前端控制器
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@RestController
@RequestMapping("/admin/sys/ware")
@CrossOrigin
public class WareController {
    @Resource
    private WareService wareService;

    @ApiOperation(value = "获取全部仓库")
    @GetMapping("findAllList")
    public Result findAllList() {
        return Result.ok(wareService.list());
    }
}

