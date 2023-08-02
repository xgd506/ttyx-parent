package hue.xgd.ttyx.product.service.impl;

import hue.xgd.ttyx.model.product.Category;
import hue.xgd.ttyx.product.mapper.CategoryMapper;
import hue.xgd.ttyx.product.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
