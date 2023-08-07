package hue.xgd.ttyx.search.repositor;

import hue.xgd.ttyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:02
 * @Description:
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);
    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    Page<SkuEs> findByWareIdAndSkuNameContaining(Long wareId, String keyword, Pageable pageable);
}
