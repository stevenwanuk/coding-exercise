package com.sven.ce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
{
    @Bean
    public WebMvcConfigurer forwardToIndex()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addViewControllers(ViewControllerRegistry registry)
            {
                // forward root request to swagger welcome page
                registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
            }
        };
    }
}
