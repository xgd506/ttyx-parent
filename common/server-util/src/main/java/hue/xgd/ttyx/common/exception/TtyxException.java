package hue.xgd.ttyx.common.exception;

import hue.xgd.ttyx.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @Author:xgd
 * @Date:2023/8/1 10:25
 * @Description: 自定义异常类
 */
@Data
public class TtyxException extends RuntimeException{
    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public TtyxException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public TtyxException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "TtyxException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
