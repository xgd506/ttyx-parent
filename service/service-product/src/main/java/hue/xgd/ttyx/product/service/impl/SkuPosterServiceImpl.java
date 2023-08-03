package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hue.xgd.ttyx.model.product.SkuPoster;
import hue.xgd.ttyx.product.mapper.SkuPosterMapper;
import hue.xgd.ttyx.product.service.SkuPosterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster> implements SkuPosterService {

    @Override
    public List<SkuPoster> findBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuPoster> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId,id);
        List<SkuPoster> skuPosterList = baseMapper.selectList(wrapper);
        return skuPosterList;
    }

    @Override
    public void removeBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuPoster> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId,id);
        baseMapper.delete(wrapper);
    }

    @Override
    public void removeBatchSkuInfo(List<Long> idList) {
        for (Long skuId:idList
             ) {
            LambdaQueryWrapper<SkuPoster> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(SkuPoster::getSkuId,skuId);
            baseMapper.delete(wrapper);
        }
    }
}
