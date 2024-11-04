package cc.jcguzman.petadoptionapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Add multiple view controllers
        registry.addViewController("/swagger").setViewName("redirect:/swagger-ui.html");
        registry.addViewController("/docs").setViewName("redirect:/swagger-ui.html");
        registry.addViewController("/api-docs").setViewName("redirect:/swagger-ui.html");
    }
}