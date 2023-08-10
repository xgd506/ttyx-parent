package hue.xgd.ttyx.product.mapper;

import hue.xgd.ttyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * sku信息 Mapper 接口
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Repository
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    Integer unlockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    SkuInfo checkStock(@Param(("skuId")) Long skuId, @Param("skuNum") Integer skuNum);

    Integer lockStock(@Param(("skuId")) Long skuId, @Param("skuNum") Integer skuNum);

    Integer minusStock(@Param("skuId")Long skuId, @Param("skuNum")Integer skuNum);
}
