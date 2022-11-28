package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport {

  @Autowired
  LoginInterceptor loginInterceptor;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("access-control-allow-headers",
            "access-control-allow-methods",
            "access-control-allow-origin",
            "access-control-max-age",
            "X-Frame-Options")
        // 启用后会导致websocket跨域配置失效
        .allowCredentials(false)
        .maxAge(3600);
  }

  @Override
  protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      // 解决 Controller 返回普通文本中文乱码问题
      if (converter instanceof StringHttpMessageConverter) {
        ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
      }

      // 解决 Controller 返回json对象中文乱码问题
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        ((MappingJackson2HttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
      }
    }
  }



  /**
   * 添加token拦截器
   *
   * @param registry
   */
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(loginInterceptor)
//        //选择过滤哪些接口
//        .addPathPatterns("/**")
//        //选择忽略的接口
//        .excludePathPatterns("/login");
//    super.addInterceptors(registry);
//  }

}
