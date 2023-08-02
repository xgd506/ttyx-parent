package hue.xgd.ttyx.sys.service;

import hue.xgd.ttyx.model.sys.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface RegionService extends IService<Region> {

    List<Region> findRegionByKeyword(String keyword);
}
