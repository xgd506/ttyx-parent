package hue.xgd.ttyx.product.service;

import hue.xgd.ttyx.model.product.SkuPoster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface SkuPosterService extends IService<SkuPoster> {

    List<SkuPoster> findBySkuInfo(Long id);

    void removeBySkuInfo(Long id);

    void removeBatchSkuInfo(List<Long> idList);
}
