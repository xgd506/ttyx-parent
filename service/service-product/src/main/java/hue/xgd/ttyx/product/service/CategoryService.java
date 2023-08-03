package hue.xgd.ttyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.vo.product.CategoryQueryVo;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface CategoryService extends IService<Category> {

    IPage<Category> selectCategory(Page<Category> pageParams, CategoryQueryVo categoryQueryVo);

    List<Category> findAllList();
}
