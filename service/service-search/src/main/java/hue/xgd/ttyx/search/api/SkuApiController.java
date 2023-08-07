package hue.xgd.ttyx.search.api;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.search.service.SkuService;
import hue.xgd.ttyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/5 18:00
 * @Description:
 */
@RestController
@RequestMapping("api/search/sku")
public class SkuApiController {
    @Resource
    private SkuService skuService;

    @ApiOperation(value = "获取爆品商品")
    @GetMapping("inner/findHotSkuList")
    public List<SkuEs> findHotSkuList() {
        return skuService.findHotSkuList();
    }
    //查询分类中对应的商品 需要调用Es
    @ApiOperation(value = "搜索商品")
    @GetMapping("{page}/{limit}")
    public Result list(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            SkuEsQueryVo searchParamVo) {

        Pageable pageable = PageRequest.of(page-1, limit);
        Page<SkuEs> pageModel =  skuService.search(pageable, searchParamVo);
        return Result.ok(pageModel);
    }
}
