package hue.xgd.ttyx.vo.product;

import hue.xgd.ttyx.model.product.SkuAttrValue;
import hue.xgd.ttyx.model.product.SkuImage;
import hue.xgd.ttyx.model.product.SkuInfo;
import hue.xgd.ttyx.model.product.SkuPoster;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SkuInfoVo extends SkuInfo {

	@ApiModelProperty(value = "海报列表")
	private List<SkuPoster> skuPosterList;

	@ApiModelProperty(value = "属性值")
	private List<SkuAttrValue> skuAttrValueList;

	@ApiModelProperty(value = "图片")
	private List<SkuImage> skuImagesList;

}

