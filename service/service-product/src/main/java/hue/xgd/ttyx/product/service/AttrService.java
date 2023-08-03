package hue.xgd.ttyx.product.service;

import hue.xgd.ttyx.model.product.Attr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
public interface AttrService extends IService<Attr> {

    List<Attr> selectByGroupId(Long groupId);
}
