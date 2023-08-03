package hue.xgd.ttyx.product.service;

import hue.xgd.ttyx.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> findBySkuInfo(Long id);

    void removeBySkuInfo(Long id);

    void removeBatchSkuInfo(List<Long> idList);
}
