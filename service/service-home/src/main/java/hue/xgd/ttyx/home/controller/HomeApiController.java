package hue.xgd.ttyx.home.controller;

import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.common.security.AuthContextHolder;
import hue.xgd.ttyx.home.service.HomeService;
import hue.xgd.ttyx.model.search.SkuEs;
import hue.xgd.ttyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/5 17:02
 * @Description:
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("api/home")
public class HomeApiController {
    @Resource
    private HomeService homeService;

    @ApiOperation(value = "获取首页数据")
    @GetMapping("index")
    public Result index(HttpServletRequest request) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> result= homeService.homeData(userId);
        return Result.ok(result);
    }
}
