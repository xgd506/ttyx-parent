package hue.xgd.ttyx.home.service.impl;

import hue.xgd.ttyx.client.product.ProductFeignClient;
import hue.xgd.ttyx.client.search.SearchClient;
import hue.xgd.ttyx.client.user.UserFeignClient;
import hue.xgd.ttyx.home.service.HomeService;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/5 17:08
 * @Description:
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private UserFeignClient feignClient;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private SearchClient searchClient;
    @Override
    public Map<String, Object> homeData(Long userId) {
        Map<String,Object> result=new HashMap<>();
        //获取提货点信息
        LeaderAddressVo leaderAddressVo = feignClient.getUserAddressByUserId(userId);
        result.put("leaderAddressVo",leaderAddressVo);
        //新人专享
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList",newPersonSkuInfoList);
        //所有分类
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList",categoryList);
        //获取热销好货
        List<SkuEs> hotSkuList = searchClient.findHotSkuList();
        result.put("hotSkuList",hotSkuList);
        return result;
    }
}
