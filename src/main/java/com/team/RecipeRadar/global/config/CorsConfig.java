package com.team.RecipeRadar.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // Access-Control-Allow-Origin
        config.addAllowedHeader("*");  // Access-Control-Request-Headers
        config.addAllowedMethod("*"); // Access-Control-Request-Method

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
