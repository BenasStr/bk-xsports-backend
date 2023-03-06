package com.ktu.xsports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HostConfiguration {
    @Value("${project.api.url}")
    private String url;

    @Bean
    public String getUrl(){
        return url;
    }
}
