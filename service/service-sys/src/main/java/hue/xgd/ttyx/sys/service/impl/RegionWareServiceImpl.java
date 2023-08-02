package hue.xgd.ttyx.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.exception.TtyxException;
import hue.xgd.ttyx.common.result.ResultCodeEnum;
import hue.xgd.ttyx.model.sys.RegionWare;
import hue.xgd.ttyx.sys.mapper.RegionWareMapper;
import hue.xgd.ttyx.sys.service.RegionWareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.vo.sys.RegionWareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Override
    public IPage<RegionWare> selectAdminPage(Page<RegionWare> pageParams, RegionWareQueryVo adminQueryVo) {
        String keyword = adminQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like(RegionWare::getRegionName,keyword)
                    .or().like(RegionWare::getWareName,keyword);
        }
        IPage<RegionWare> regionWarePage = baseMapper.selectPage(pageParams, wrapper);
        return regionWarePage;
    }

    @Override
    public void saveRegionWare(RegionWare regionWare) {
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new TtyxException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}
