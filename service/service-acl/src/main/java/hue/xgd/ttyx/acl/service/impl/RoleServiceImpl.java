package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.RoleMapper;
import hue.xgd.ttyx.acl.service.RoleService;
import hue.xgd.ttyx.model.acl.Role;
import hue.xgd.ttyx.vo.acl.RoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author:xgd
 * @Date:2023/8/1 15:25
 * @Description:
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
         //获得传递过来的角色值
        String roleName = roleQueryVo.getRoleName();
        //创建mq条件对象
        LambdaQueryWrapper<Role> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(roleName)){
            //模糊匹配 role_name like ?(%roleName%)
            wrapper.like(Role::getRoleName,roleName);
        }
        //调用方法实现条件查询
        Page<Role> page = baseMapper.selectPage(pageParam, wrapper);
        //返回查询结果
        return page;
    }
}
