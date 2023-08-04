package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.*;
import hue.xgd.ttyx.product.mapper.SkuInfoMapper;
import hue.xgd.ttyx.product.service.SkuAttrValueService;
import hue.xgd.ttyx.product.service.SkuImageService;
import hue.xgd.ttyx.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.product.service.SkuPosterService;
import hue.xgd.ttyx.vo.product.SkuInfoQueryVo;
import hue.xgd.ttyx.vo.product.SkuInfoVo;
import org.springframework.beans.BeanUtils;
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


}
