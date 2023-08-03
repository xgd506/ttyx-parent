package hue.xgd.ttyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.product.mapper.CategoryMapper;
import hue.xgd.ttyx.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hue.xgd.ttyx.vo.product.CategoryQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public IPage<Category> selectCategory(Page<Category> pageParams, CategoryQueryVo categoryQueryVo) {
        String name = categoryQueryVo.getName();
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.like(Category::getName,name);
        }
        Page<Category> categoryPage = baseMapper.selectPage(pageParams, wrapper);
        return categoryPage;
    }

    @Override
    public List<Category> findAllList() {
       LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort); //升序排列
       // List<Category> categoryList = baseMapper.selectList(queryWrapper);
       //  return categoryList;
        return this.list(queryWrapper);
    }
}
