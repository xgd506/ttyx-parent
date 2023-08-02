package hue.xgd.ttyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.model.sys.Region;
import hue.xgd.ttyx.sys.mapper.RegionMapper;
import hue.xgd.ttyx.sys.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper,Region> implements RegionService {

    @Override
    public List<Region> findRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Region::getName, keyword);
        return baseMapper.selectList(queryWrapper);
    }
}
