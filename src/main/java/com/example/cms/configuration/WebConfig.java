package com.example.cms.configuration;

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
        registry.addMapping("/**")  // Cho phép tất cả API
            .allowedOrigins("*") // Cho phép tất cả origin (có thể thay * bằng ngrok domain cho an toàn hơn)
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders("ngrok-skip-browser-warning")
            .allowCredentials(false);
      }
    };
  }
}
