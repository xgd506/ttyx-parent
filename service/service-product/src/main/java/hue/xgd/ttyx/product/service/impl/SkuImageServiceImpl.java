package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hue.xgd.ttyx.model.product.SkuImage;
import hue.xgd.ttyx.product.mapper.SkuImageMapper;
import hue.xgd.ttyx.product.service.SkuImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {

    @Override
    public List<SkuImage> findBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId,id);
        List<SkuImage> skuImages = baseMapper.selectList(wrapper);
        return skuImages;
    }

    @Override
    public void removeBySkuInfo(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId,id);
        baseMapper.delete(wrapper);
    }

    @Override
    public void removeBatchSkuInfo(List<Long> idList) {
        for (Long skuId:idList
        ) {
            LambdaQueryWrapper<SkuImage> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(SkuImage::getSkuId,skuId);
            baseMapper.delete(wrapper);
        }
    }
}
