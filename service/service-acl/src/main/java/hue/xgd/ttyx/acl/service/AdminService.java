package hue.xgd.ttyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.model.acl.Admin;
import hue.xgd.ttyx.vo.acl.AdminQueryVo;

/**
 * @Author:xgd
 * @Date:2023/8/1 17:06
 * @Description:
 */
public interface AdminService extends IService<Admin> {
    IPage<Admin> selectAdminPage(Page<Admin> pageParams, AdminQueryVo adminQueryVo);
}
