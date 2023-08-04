package hue.xgd.ttyx.activity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.activity.ActivityInfo;
import hue.xgd.ttyx.activity.service.ActivityInfoService;
import hue.xgd.ttyx.vo.activity.ActivityRuleVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:31
 * @Description:
 */
@RestController
@RequestMapping("/admin/activity/activityInfo")
@CrossOrigin
public class ActivityInfoController {
    @Resource
    private ActivityInfoService activityInfoService;
    @GetMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Long page,
                              @PathVariable Long limit) {
        Page<ActivityInfo> pageParams = new Page<>(page, limit);
        IPage<ActivityInfo> pageModel = activityInfoService.selectAdminPage(pageParams);
        return Result.ok(pageModel);
    }
    @ApiOperation(value = "获取活动")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ActivityInfo activityInfo = activityInfoService.getById(id);
        activityInfo.setActivityTypeString(activityInfo.getActivityType().getComment());
        return Result.ok(activityInfo);
    }

    @ApiOperation(value = "新增活动")
    @PostMapping("save")
    public Result save(@RequestBody ActivityInfo activityInfo) {
        activityInfo.setCreateTime(new Date());
        activityInfoService.save(activityInfo);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改活动")
    @PutMapping("update")
    public Result updateById(@RequestBody ActivityInfo activityInfo) {
        activityInfoService.updateById(activityInfo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除活动")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        activityInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value="根据id列表删除活动")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        activityInfoService.removeByIds(idList);
        return Result.ok(null);
    }
    @ApiOperation(value = "获取活动规则")
    @GetMapping("findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable Long id) {
        return Result.ok(activityInfoService.findActivityRuleList(id));
    }

    @ApiOperation(value = "新增活动规则")
    @PostMapping("saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo) {
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok(null);
    }

    /**
     * 根据关键字获取sku列表，活动使用
     * @param keyword
     * @return
     */
    @GetMapping("findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return Result.ok(activityInfoService.findSkuInfoByKeyword(keyword));
    }
}
