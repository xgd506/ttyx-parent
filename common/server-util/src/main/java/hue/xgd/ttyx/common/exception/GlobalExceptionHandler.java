package hue.xgd.ttyx.common.exception;

import hue.xgd.ttyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:xgd
 * @Date:2023/8/1 10:18
 * @Description:
 */
@ControllerAdvice //全局的控制器异常处理器
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class) //异常处理方法
    @ResponseBody //返回json数据
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }

    @ExceptionHandler(TtyxException.class) //异常处理方法
    @ResponseBody //返回json数据
    public Result error(TtyxException e){
        e.printStackTrace();
        return Result.fail(null);
    }
}
