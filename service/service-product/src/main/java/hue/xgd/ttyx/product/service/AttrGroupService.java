package hue.xgd.ttyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hue.xgd.ttyx.model.product.AttrGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import hue.xgd.ttyx.vo.product.AttrGroupQueryVo;

import java.util.List;

/**
 * <p>
 * 属性分组 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface AttrGroupService extends IService<AttrGroup> {

    IPage<AttrGroup> selectCategory(Page<AttrGroup> pageParams, AttrGroupQueryVo attrGroupQueryVo);

    List<AttrGroup> findAllList();
}
