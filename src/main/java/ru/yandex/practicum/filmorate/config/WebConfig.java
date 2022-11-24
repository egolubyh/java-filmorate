package ru.yandex.practicum.filmorate.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.servletcache.ContentCachingFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<ContentCachingFilter> config() {
        FilterRegistrationBean<ContentCachingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ContentCachingFilter());
        registration.addUrlPatterns("/reviews/*");
        return registration;
    }

}
