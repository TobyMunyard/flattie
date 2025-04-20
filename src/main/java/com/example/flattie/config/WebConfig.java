package com.example.flattie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

// This configuration allows you to serve static files from the uploads directory in your project root.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    // This method is used to add resource handlers for serving static files.
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Map /uploads/** to the physical uploads/ folder in project root
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}