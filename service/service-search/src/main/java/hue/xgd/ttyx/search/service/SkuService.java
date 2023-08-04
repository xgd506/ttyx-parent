package hue.xgd.ttyx.search.service;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:00
 * @Description:
 */
public interface SkuService {
    void upperSku(Long skuId);

    void lowerSku(Long skuId);

    void deleteSku(Long skuId);

    void BatchdDleteSku(List<Long> skuId);
}
