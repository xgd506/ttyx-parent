package hue.xgd.ttyx.acl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.acl.AdminRole;

import java.util.List;

/**
 * @Author:xgd
 * @Date:2023/8/1 17:32
 * @Description:
 */
public interface AdminRoleService extends IService<AdminRole> {


    List<AdminRole> List(LambdaQueryWrapper<AdminRole> wrapper);
}
