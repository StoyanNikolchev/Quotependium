package com.softuni.quotependium.configs;

import com.softuni.quotependium.interceptors.RequestLoggingInterceptor;
import com.softuni.quotependium.interceptors.RequestTimeMetricsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final RequestLoggingInterceptor requestLoggingInterceptor;
    private final RequestTimeMetricsInterceptor requestTimeMetricsInterceptor;

    public WebMvcConfig(RequestLoggingInterceptor requestLoggingInterceptor, RequestTimeMetricsInterceptor requestTimeMetricsInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
        this.requestTimeMetricsInterceptor = requestTimeMetricsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
        registry.addInterceptor(requestTimeMetricsInterceptor);
    }
}