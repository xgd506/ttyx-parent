package hue.xgd.ttyx.home.service.impl;

import hue.xgd.ttyx.client.activity.ActivityFeignClient;
import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.client.search.SearchClient;
import hue.xgd.ttyx.home.service.ItemService;
import hue.xgd.ttyx.model.activity.CouponInfo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Author:xgd
 * @Date:2023/8/7 10:30
 * @Description:
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private ActivityFeignClient activityFeignClient;
    @Resource
    private SearchClient searchClient;
    @Override
    public Map<String, Object> item(Long skuId, Long userId) {
        Map<String,Object> result=new HashMap<>();
        CompletableFuture<SkuInfoVo> TaskA = CompletableFuture.supplyAsync(() -> {
            //1.获得Sku的信息+图片+海报
            // SkuInfoVo skuInfoVo = productFeignClient.get(id);
            SkuInfoVo skuInfoVo = productFeignClient.get(skuId);
            result.put("skuInfoVo",skuInfoVo);
            return skuInfoVo;
        }, threadPoolExecutor);
        CompletableFuture<Map<String, Object>> TaskB = CompletableFuture.supplyAsync(() -> {
            //2.获得优惠券信息
            Map<String, Object> map = activityFeignClient.findCoupon(skuId, userId);
            result.putAll(map);
            return map;
        }, threadPoolExecutor);

        //3.更新热度 点击一次商品 热度分+1
        CompletableFuture<Void> TaskC = CompletableFuture.runAsync(() -> {
            searchClient.incrHotScore(skuId);
        }, threadPoolExecutor);
        //
        CompletableFuture.allOf(
                TaskA,
                TaskB,
                TaskC
        );
        return null;
    }
}
