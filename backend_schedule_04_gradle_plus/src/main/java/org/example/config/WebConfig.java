package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 URL에 대해
                        .allowedOrigins("http://localhost:3000", "http://localhost:8081") // Flutter 앱 주소
                        .allowedMethods("*"); // GET, POST, PUT, DELETE 등 모두 허용
            }
        };
    }
}
