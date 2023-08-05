package hue.xgd.ttyx.user.controller;

import com.alibaba.fastjson.JSONObject;
import hue.xgd.ttyx.common.constant.RedisConst;
import hue.xgd.ttyx.common.exception.TtyxException;
import hue.xgd.ttyx.common.result.Result;
import hue.xgd.ttyx.common.result.ResultCodeEnum;
import hue.xgd.ttyx.enums.UserType;
import hue.xgd.ttyx.model.user.User;
import hue.xgd.ttyx.user.service.UserService;
import hue.xgd.ttyx.user.utils.ConstantPropertiesUtil;
import hue.xgd.ttyx.user.utils.HttpClientUtils;
import hue.xgd.ttyx.util.JwtHelper;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import hue.xgd.ttyx.vo.user.UserLoginVo;
import io.swagger.annotations.Api;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author:xgd
 * @Date:2023/8/5 10:53
 * @Description:/api/user/weixin/wxLogin/0d1lO50w3Fsj713b9p1w3DBQg03lO50x"
 */
@Api(tags = "登陆")
@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/wxLogin/{code}")
    public Result loginWx(@PathVariable String code){
        //appId+appKey+code 发送到微信服务接口
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        //HttpClients通过get请求
        //拼接url  地址?参数=value&参数=value
        StringBuffer url=new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl = String.format(url.toString(),
                wxOpenAppId, wxOpenAppSecret, code);
       // String result = HttpClientUtils.get(accessTokenUrl);
        String result=null;
        //返回的session_key+openId
        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            throw new TtyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //openId是微信的唯一标识
        JSONObject jsonObject = JSONObject.parseObject(result);
        //String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");
        //添加微信用户数据到数据库中 user表
        //判断是否是第一次登陆根据是否有openid
        User user= userService.getUserByOpenId(openid);
        if(user==null){
            user=new User();
            user.setOpenId(openid);
            user.setNickName(openid);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }

        //根据用户id查询提货点+团长信息
        //提货点 user表---》user_delivery---》leaderId
        //团长 leader表---》团长信息
        //封装到LeadAddressVo中默认地址+团长信息
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressVoByUserId(user.getId());
        Map<String,Object> map=new HashMap<>();
        String nickName = user.getNickName();
        map.put("user",user);
        map.put("leaderAddressVo",leaderAddressVo);
        //JWT+userId+userName--->token
        String token = JwtHelper.createToken(user.getId(), nickName);
        map.put("token",token);
        //将登陆用户信息放到redis中
        UserLoginVo userLoginVo = this.userService.getUserLoginVo(user.getId());
        redisTemplate.opsForValue().
                set(RedisConst.USER_LOGIN_KEY_PREFIX + user.getId(), userLoginVo, RedisConst.USERKEY_TIMEOUT, TimeUnit.DAYS);;
        //封装返回数据Map
        return Result.ok(map);
    }

}
