package hue.xgd.ttyx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hue.xgd.ttyx.model.user.Leader;
import hue.xgd.ttyx.model.user.User;
import hue.xgd.ttyx.model.user.UserDelivery;
import hue.xgd.ttyx.user.mapper.LeaderMapper;
import hue.xgd.ttyx.user.mapper.UserDeliveryMapper;
import hue.xgd.ttyx.user.mapper.UserMapper;
import hue.xgd.ttyx.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.vo.user.LeaderAddressVo;
import hue.xgd.ttyx.vo.user.UserLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserDeliveryMapper userDeliveryMapper;
    @Resource
    private LeaderMapper leaderMapper;
    @Override
    public User getUserByOpenId(String openid) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenId,openid);
        User user = baseMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public LeaderAddressVo getLeaderAddressVoByUserId(Long id) {
        //一个用户的默认收货地址+团长信息
        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, id).eq(UserDelivery::getIsDefault, 1));
        if(null == userDelivery) return null;
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        LeaderAddressVo leaderAddressVo=new LeaderAddressVo();
        BeanUtils.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setUserId(id);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVo(Long id) {
        UserLoginVo userLoginVo = new UserLoginVo();
        User user = this.getById(id);
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setUserId(id);
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        userLoginVo.setOpenId(user.getOpenId());
        userLoginVo.setIsNew(user.getIsNew());
        //
        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, id).eq(UserDelivery::getIsDefault, 1));
        if(userDelivery!=null){
            userLoginVo.setLeaderId(userLoginVo.getLeaderId());
            userLoginVo.setWareId(userLoginVo.getWareId());
        }
        return userLoginVo;
    }
}
