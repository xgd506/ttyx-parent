package hue.xgd.ttyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.activity.ActivityInfo;
import hue.xgd.ttyx.model.activity.ActivityRule;
import hue.xgd.ttyx.vo.activity.ActivityRuleVo;

import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:33
 * @Description:
 */
public interface ActivityInfoService extends IService<ActivityInfo> {
    IPage<ActivityInfo> selectAdminPage(Page<ActivityInfo> pageParams);

    Map<String,Object> findActivityRuleList(Long id);

    void saveActivityRule(ActivityRuleVo activityRuleVo);

    Object findSkuInfoByKeyword(String keyword);
}
