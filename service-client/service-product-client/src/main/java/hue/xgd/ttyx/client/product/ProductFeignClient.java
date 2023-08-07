package hue.xgd.ttyx.client.product;

import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/3 16:18
 * @Description:
 */
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Long categoryId);

    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);

    @PostMapping("/api/product/inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    @GetMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword);
    @PostMapping("/api/product/inner/findCategoryList")
    List<Category> findCategoryList(@RequestBody List<Long> rangeIdList);
    @GetMapping("/api/product/inner/findAllCategoryList")
    List<Category> findAllCategoryList();
    /**
     * 获取新人专享
     * @return
     */
    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList();


    @GetMapping("/api/product/inner/get/{id}")
    SkuInfoVo get(@PathVariable Long id);
}
