package hue.xgd.ttyx.model.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import hue.xgd.ttyx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "OrderReturnReason")
@TableName("order_return_reason")
public class OrderReturnReason extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "退货类型")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "sort")
	@TableField("sort")
	private Integer sort;

	@ApiModelProperty(value = "状态：0->不启用；1->启用")
	@TableField("status")
	private Integer status;

}