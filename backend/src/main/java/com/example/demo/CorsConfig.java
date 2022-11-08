package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport {

  @Autowired
  LoginInterceptor loginInterceptor;

  /**
   * 解决跨域请求问题
   *
   * @param registry
   */
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")
//        .allowedOrigins("*")
//        .allowCredentials(true)
//        .allowedMethods("GET", "POST", "DELETE", "PUT")
//        .maxAge(3600);
//  }

  /**
   * 添加token拦截器
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor)
        //选择过滤哪些接口
        .addPathPatterns("/**")
        //选择忽略的接口
        .excludePathPatterns("/login");
    super.addInterceptors(registry);
  }

}
