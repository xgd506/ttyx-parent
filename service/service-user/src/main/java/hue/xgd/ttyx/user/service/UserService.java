package hue.xgd.ttyx.user.service;

import hue.xgd.ttyx.model.user.User;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import hue.xgd.ttyx.vo.user.UserLoginVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-05
 */
public interface UserService extends IService<User> {

    User getUserByOpenId(String openid);

    LeaderAddressVo getLeaderAddressVoByUserId(Long id);

    UserLoginVo getUserLoginVo(Long id);
}
