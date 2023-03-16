package com.ktu.xsports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedMethods(
                        RequestMethod.GET.name(),
                        RequestMethod.POST.name(),
                        RequestMethod.PUT.name(),
                        RequestMethod.DELETE.name())
                    .allowedOrigins("http://localhost:3000");
            }
        };
    }
}
