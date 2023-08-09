package hue.xgd.ttyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.product.SkuInfo;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:37
 * @Description:
 */
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {
    List<CouponInfo> selectCouponInfoList(SkuInfo skuInfo, Long categoryId, Long userId);

    List<CouponInfo> selectCartCouponInfoList(Long userId);
}
