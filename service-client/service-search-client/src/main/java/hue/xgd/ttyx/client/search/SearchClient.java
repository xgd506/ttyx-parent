package hue.xgd.ttyx.client.search;

import hue.xgd.ttyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/5 18:09
 * @Description:
 */
@FeignClient("service-search")
public interface SearchClient {

    @GetMapping("/api/search/sku/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList();
}
