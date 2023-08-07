package hue.xgd.ttyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hue.xgd.ttyx.model.activity.CouponUse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:43
 * @Description:
 */
public interface CouponUseMapper extends BaseMapper<CouponUse> {
    List<CouponUse> getCouponInfoByUseId(@Param("userId") Long userId);
}
