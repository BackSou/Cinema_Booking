package com.example.cinema;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mở cửa cho TẤT CẢ các API (Bao gồm cả /api/login, /api/movies...)
                .allowedOrigins("*") // Cho phép mọi nguồn truy cập (kể cả Live Server 127.0.0.1:5500)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // BẮT BUỘC phải có OPTIONS để xử lý Preflight
                .allowedHeaders("*"); // Cho phép mọi loại dữ liệu (JSON, FormData...)
    }
}