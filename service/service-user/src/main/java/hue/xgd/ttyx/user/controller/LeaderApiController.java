package hue.xgd.ttyx.user.controller;

import hue.xgd.ttyx.user.service.LeaderService;
import hue.xgd.ttyx.user.service.UserService;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:xgd
 * @Date:2023/8/5 16:55
 * @Description:
 */
@Api(tags = "团长接口")
@RestController
@RequestMapping("/api/user/leader")

public class LeaderApiController {
    @Resource
    private UserService userService;

    @Resource
    private LeaderService leaderService;

    @ApiOperation("提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable(value = "userId") Long userId) {
        return userService.getLeaderAddressVoByUserId(userId);
    }
}
