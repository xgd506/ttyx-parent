package hue.xgd.ttyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hue.xgd.ttyx.model.activity.ActivityInfo;
import hue.xgd.ttyx.model.activity.ActivityRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:35
 * @Description:
 */
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {
    List<Long> selectSkuIdListExist(@Param("skuIdList") List<Long> skuIdList);

    List<ActivityRule> findActivityRule(@Param("skuId")Long skuId);
}
