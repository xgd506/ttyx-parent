package hue.xgd.ttyx.common.result;

import lombok.Data;
import lombok.Getter;

/**
 * @Author:xgd
 * @Date:2023/8/1 10:00
 * @Description: 结果返回格式
 * 泛型
 */
@Data
public class Result<T> {
    //状态码
    private Integer code;
    //信息
    private String message;
    //具体数据   泛型
    private T data;

    //构造私有化 使用静态方法调用
    private Result(){}
    //<T> 表示泛型方法
    public static <T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result=new Result<>();
        //判断返回数据是否存在
        if(data!=null){
            result.setData(data);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> Result<T> ok(T data){
        return  build(data,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(T data){
        return  build(data,ResultCodeEnum.FAIL);
    }

}
