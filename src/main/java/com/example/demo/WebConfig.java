//package com.example.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
////@Configuration
////public class WebConfig {
////
////    // 过滤器跨域配置
////    @Bean
////    public CorsFilter corsFilter() {
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////
////        CorsConfiguration config = new CorsConfiguration();
////
////        // 允许跨域的头部信息
////        config.addAllowedHeader("*");
////        // 允许跨域的方法
////        config.addAllowedMethod("*");
////        // 可访问的外部域
////        config.addAllowedOrigin("*");
////        // 需要跨域用户凭证（cookie、HTTP认证及客户端SSL证明等）
////        //config.setAllowCredentials(true);
////        //config.addAllowedOriginPattern("*");
////
////        // 跨域路径配置
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
////}
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
////    @Autowired
////    ResponseResultInterceptor responseResultInterceptor;
//
//    /**
//     * 跨域配置
//     *
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("access-control-allow-headers",
//                        "access-control-allow-methods",
//                        "access-control-allow-origin",
//                        "access-control-max-age",
//                        "X-Frame-Options")
//                // 启用后会导致websocket跨域配置失效
//                .allowCredentials(false)
//                .maxAge(3600);
//    }
//}
