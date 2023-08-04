package hue.xgd.ttyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.vo.product.SkuInfoQueryVo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> selectCategory(Page<SkuInfo> pageParams, SkuInfoQueryVo skuInfoQueryVo);

    void saveSkuInfo(SkuInfoVo skuInfoVo);

    SkuInfoVo getSkuInfoVo(Long id);

    void updateSkuInfo(SkuInfoVo skuInfoVo);

    void removeSkuInfo(Long id);

    void removeBatchSkuInfo(List<Long> idList);

    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    List<SkuInfo> findSkuInfoByKeyword(String keyword);
}
