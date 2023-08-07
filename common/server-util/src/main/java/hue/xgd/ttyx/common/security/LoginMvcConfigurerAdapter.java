package hue.xgd.ttyx.common.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/**
 * @Author:xgd
 * @Date:2023/8/5 16:18
 * @Description:
 */
import javax.annotation.Resource;

@Configuration
public class LoginMvcConfigurerAdapter extends WebMvcConfigurationSupport {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器，设置路径
        registry.addInterceptor(
                        new UserLoginInterceptor(redisTemplate))
                .addPathPatterns("/api/**")//拦截有api的url
                .excludePathPatterns("/api/user/weixin/wxLogin/*");//排除登录接口
        super.addInterceptors(registry);
    }
}