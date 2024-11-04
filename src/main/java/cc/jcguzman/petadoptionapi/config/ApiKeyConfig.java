package cc.jcguzman.petadoptionapi.config;

import cc.jcguzman.petadoptionapi.service.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ApiKeyConfig implements WebMvcConfigurer {

    private final ApiKeyService apiKeyService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiKeyInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/v1/keys/generate"  // Exclude key generation endpoint
                );
    }

    private class ApiKeyInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler) throws Exception {
            String path = request.getRequestURI();
            if (path.startsWith("/swagger-ui/") ||
                    path.startsWith("/v3/api-docs/") ||
                    path.equals("/api/v1/keys/generate")) {
                return true;
            }

            String providedApiKey = request.getHeader("X-API-KEY");

            if (providedApiKey == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("API key is missing");
                return false;
            }

            if (!apiKeyService.validateKey(providedApiKey)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid or expired API key");
                return false;
            }

            return true;
        }
    }
}