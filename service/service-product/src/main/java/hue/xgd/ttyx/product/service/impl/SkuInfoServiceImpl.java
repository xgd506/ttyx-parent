package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.common.constant.RedisConst;
import hue.xgd.ttyx.common.exception.TtyxException;
import hue.xgd.ttyx.common.result.ResultCodeEnum;
import hue.xgd.ttyx.model.product.*;
import hue.xgd.ttyx.product.mapper.SkuInfoMapper;
import hue.xgd.ttyx.product.service.SkuAttrValueService;
import hue.xgd.ttyx.product.service.SkuImageService;
import hue.xgd.ttyx.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.product.service.SkuPosterService;
import hue.xgd.ttyx.vo.product.SkuInfoQueryVo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import hue.xgd.ttyx.vo.product.SkuStockLockVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Resource
    private SkuAttrValueService attrValueService;
    @Resource
    private SkuImageService skuImageService;
    @Resource
    private SkuPosterService skuPosterService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public IPage<SkuInfo> selectCategory(Page<SkuInfo> pageParams, SkuInfoQueryVo skuInfoQueryVo) {
        String keyword = skuInfoQueryVo.getKeyword();
        String skuType = skuInfoQueryVo.getSkuType();
        Long categoryId = skuInfoQueryVo.getCategoryId();
        LambdaQueryWrapper<SkuInfo> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)){
            wrapper.like(SkuInfo::getSkuName,keyword);
        }
        if(!StringUtils.isEmpty(skuType)){
            wrapper.like(SkuInfo::getSkuType,skuType);
        }
        if(!StringUtils.isEmpty(categoryId)){
            wrapper.like(SkuInfo::getCategoryId,categoryId);
        }
        Page<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParams, wrapper);
        return skuInfoPage;
    }

    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //保存基本信息
        SkuInfo skuInfo=new SkuInfo();
        //skuInfo.setSkuName(skuInfoVo.getSkuName());
        //skuInfo.setSkuCode(skuInfo.getSkuCode());
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.insert(skuInfo);
        //2将属性信息保存到sku_att表中
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue :skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
            }
        }
        attrValueService.saveBatch(skuAttrValueList);
        //将图片信息表中到images表中
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage :skuImagesList) {
                skuImage.setSkuId(skuInfo.getId());
            }
        }
        skuImageService.saveBatch(skuImagesList);
        //将海报信息保存到poster表中
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)){
            for (SkuPoster skuPoster :skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
            }
        }
        skuPosterService.saveBatch(skuPosterList);
    }

    //得到使用信息 商品+属性+图片+海报
    @Override
    public SkuInfoVo getSkuInfoVo(Long id) {
        SkuInfoVo skuInfoVo=new SkuInfoVo();
        //sku商品信息
        SkuInfo skuInfo = baseMapper.selectById(id);
        BeanUtils.copyProperties(skuInfo,skuInfoVo);
        //属性
        List<SkuAttrValue> attrValuesList= attrValueService.findBySkuInfo(id);
        //图片
        List<SkuImage> skuImageList=skuImageService.findBySkuInfo(id);
        //海报
        List<SkuPoster> skuPosterList=skuPosterService.findBySkuInfo(id);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(attrValuesList);
        return skuInfoVo;
    }

    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {

        //更新sku商品
        Long id = skuInfoVo.getId();
        SkuInfo skuInfo=new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        baseMapper.updateById(skuInfo);
        //属性
        //删除之前的属性
        attrValueService.removeBySkuInfo(id);
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if(!CollectionUtils.isEmpty(skuAttrValueList)){
            for (SkuAttrValue skuAttrValue :skuAttrValueList) {
                skuAttrValue.setSkuId(id);
            }
        }
        attrValueService.saveBatch(skuAttrValueList);
        //图片
        skuImageService.removeBySkuInfo(id);
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if(!CollectionUtils.isEmpty(skuImagesList)){
            for (SkuImage skuImage :skuImagesList) {
                skuImage.setSkuId(id);
            }
        }
        skuImageService.saveBatch(skuImagesList);
         //海报
        skuPosterService.removeBySkuInfo(id);
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if(!CollectionUtils.isEmpty(skuPosterList)){
            for (SkuPoster skuPoster :skuPosterList) {
                skuPoster.setSkuId(id);
            }
        }
        skuPosterService.saveBatch(skuPosterList);
    }

    @Override
    public void removeSkuInfo(Long id) {
        baseMapper.deleteById(id);
        attrValueService.removeBySkuInfo(id);
        skuImageService.removeBySkuInfo(id);
        skuPosterService.removeBySkuInfo(id);
    }

    @Override
    public void removeBatchSkuInfo(List<Long> idList) {
        baseMapper.deleteBatchIds(idList);
        attrValueService.removeBatchSkuInfo(idList);
        skuImageService.removeBatchSkuInfo(idList);
        skuPosterService.removeBatchSkuInfo(idList);
    }

    @Override
    public List<SkuInfo> findSkuInfoList(List<Long> skuIdList) {
        List<SkuInfo> skuInfoList = baseMapper.selectBatchIds(skuIdList);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(SkuInfo::getSkuName,keyword);
        List<SkuInfo> skuInfoList = baseMapper.selectList(wrapper);
        return skuInfoList;
    }

    @Override
    public List<SkuInfo> findNewPersonList() {
        //商品是新人专享
        //商品是上架商品
        //只显示其中3条
        LambdaQueryWrapper<SkuInfo> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SkuInfo::getIsNewPerson,1);
        wrapper.eq(SkuInfo::getPublishStatus,1);
        wrapper.orderByDesc(SkuInfo::getStock);
        wrapper.last("LIMIT 3");
        List<SkuInfo> skuInfoList = baseMapper.selectList(wrapper);
        return skuInfoList;
    }

    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo) {
            if (CollectionUtils.isEmpty(skuStockLockVoList)){
                throw new TtyxException(ResultCodeEnum.DATA_ERROR);
            }

            // 遍历所有商品，验库存并锁库存，要具备原子性
            skuStockLockVoList.forEach(skuStockLockVo -> {
                checkLock(skuStockLockVo);
            });

            // 只要有一个商品锁定失败，所有锁定成功的商品要解锁库存
            if (skuStockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock())) {
                // 获取所有锁定成功的商品，遍历解锁库存
                skuStockLockVoList.stream().filter(SkuStockLockVo::getIsLock).forEach(skuStockLockVo -> {
                   baseMapper.unlockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
                });
                // 响应锁定状态
                return false;
            }

            // 如果所有商品都锁定成功的情况下，需要缓存锁定信息到redis。以方便将来解锁库存 或者 减库存
            // 以orderNo作为key，以lockVos锁定信息作为value
            this.redisTemplate.opsForValue().set(RedisConst.SROCK_INFO + orderNo, skuStockLockVoList);

            // 锁定库存成功之后，定时解锁库存。
            //this.rabbitTemplate.convertAndSend("ORDER_EXCHANGE", "stock.ttl", orderToken);
            return true;
    }

    private void checkLock(SkuStockLockVo skuStockLockVo){
        //公平锁，就是保证客户端获取锁的顺序，跟他们请求获取锁的顺序，是一样的。
        // 公平锁需要排队
        // ，谁先申请获取这把锁，
        // 谁就可以先获取到这把锁，是按照请求的先后顺序来的。
        RLock rLock = this.redissonClient
                .getFairLock(RedisConst.SKUKEY_PREFIX + skuStockLockVo.getSkuId());
        rLock.lock();

        try {
            // 验库存：查询，返回的是满足要求的库存列表
            SkuInfo skuInfo = baseMapper.checkStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            // 如果没有一个仓库满足要求，这里就验库存失败
            if (null == skuInfo) {
                skuStockLockVo.setIsLock(false);
                return;
            }

            // 锁库存：更新
            Integer row = baseMapper.lockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            if (row == 1) {
                skuStockLockVo.setIsLock(true);
            }
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public void minusStock(String orderNo) {
        // 获取锁定库存的缓存信息
        List<SkuStockLockVo> skuStockLockVoList = (List<SkuStockLockVo>)this.redisTemplate.opsForValue().get(RedisConst.SROCK_INFO + orderNo);
        if (CollectionUtils.isEmpty(skuStockLockVoList)){
            return ;
        }

        // 减库存
        skuStockLockVoList.forEach(skuStockLockVo -> {
            baseMapper.minusStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
        });

        // 解锁库存之后，删除锁定库存的缓存。以防止重复解锁库存
        this.redisTemplate.delete(RedisConst.SROCK_INFO + orderNo);
    }
}
