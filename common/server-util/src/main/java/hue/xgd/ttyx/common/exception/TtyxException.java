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
    private String message;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public TtyxException( Integer code,String message) {
        super(message);
        this.code = code;
        this.message=message;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public TtyxException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "TtyxException{" +
                "code=" + code +
                ", message=" + message+'\'' +
                '}';
    }
}
