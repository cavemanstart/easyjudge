package com.stone.common.config;

import com.stone.common.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration //开启后将检查登录状态
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器方法 传入我们自己的拦截器  这里的拦截器，是使用的JWT定义的拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") //拦截规则 所有
                .excludePathPatterns("/register")//例外规则
                .excludePathPatterns("/login") //例外规则
                .excludePathPatterns("/swagger-resources/**") //下面的例外规则，是swagger的
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/v2/**")
                .excludePathPatterns("/swagger-ui.html/**");
    }
}