package hue.xgd.ttyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.model.order.CartInfo;
import hue.xgd.ttyx.vo.activity.CouponRuleVo;

import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/4 14:38
 * @Description:
 */
public interface CouponInfoService extends IService<CouponInfo> {
    IPage<CouponInfo> selectPage(Page<CouponInfo> pageParam);

    CouponInfo getCouponInfo(Long id);

    Map<String,Object> findCouponRuleList(Long id);

    void saveCouponRule(CouponRuleVo couponRuleVo);

    Object findCouponByKeyword(String keyword);

    Map<String,Object> getCouponInfoByUserId(Long skuId,Long userId);

    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);

    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
}
