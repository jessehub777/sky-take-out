package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsGlobalFilterConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.addAllowedOriginPattern("*"); // ✅ 支持 withCredentials 的通配写法，替代 addAllowedOrigin("*")
        config.setAllowCredentials(true);    // ✅ 允许携带 cookie/token 等凭证
        config.addAllowedHeader("*");        // ✅ 允许所有请求头
        config.addAllowedMethod("*");        // ✅ 允许所有请求方法
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // ✅ 应用于所有路径
        
        return new CorsFilter(source);
    }
}