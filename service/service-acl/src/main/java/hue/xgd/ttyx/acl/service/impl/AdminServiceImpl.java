package hue.xgd.ttyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.acl.mapper.AdminMapper;
import hue.xgd.ttyx.acl.service.AdminService;
import hue.xgd.ttyx.model.acl.Admin;
import hue.xgd.ttyx.vo.acl.AdminQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author:xgd
 * @Date:2023/8/1 17:08
 * @Description:
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public IPage<Admin> selectAdminPage(Page<Admin> pageParams, AdminQueryVo adminQueryVo) {
        String username = adminQueryVo.getUsername();
        String name = adminQueryVo.getName();
        LambdaQueryWrapper<Admin> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(username)){
            wrapper.like(Admin::getUsername,username);
        }
        if(!StringUtils.isEmpty(name)){
            wrapper.like(Admin::getName,name);
        }
        Page<Admin> adminPage = baseMapper.selectPage(pageParams, wrapper);
        return adminPage;

    }
}
