package hue.xgd.ttyx.product.service;

import hue.xgd.ttyx.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SkuAttrValue> findBySkuInfo(Long id);

    void removeBySkuInfo(Long id);

    void removeBatchSkuInfo(List<Long> idList);
}
