package hue.xgd.ttyx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.sys.RegionWare;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.vo.sys.RegionWareQueryVo;

/**
 * <p>
 * 城市仓库关联表 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface RegionWareService extends IService<RegionWare> {

    IPage<RegionWare> selectAdminPage(Page<RegionWare> pageParams, RegionWareQueryVo adminQueryVo);

    void saveRegionWare(RegionWare regionWare);

    void updateStatus(Long id, Integer status);
}
