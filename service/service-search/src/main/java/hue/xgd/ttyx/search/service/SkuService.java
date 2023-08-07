package hue.xgd.ttyx.search.service;

import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    List<SkuEs> findHotSkuList();

    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo searchParamVo);

    void incrHotScore(Long skuId);
}
