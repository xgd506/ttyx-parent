package hue.xgd.ttyx.search.repositor;

import hue.xgd.ttyx.model.search.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:02
 * @Description:
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {
}
