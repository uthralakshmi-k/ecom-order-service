package com.practise.ops.config;

import com.practise.ops.common.ApiResponse; // your ApiResponse class
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper; // use Spring-managed ObjectMapper

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestKey = request.getHeader("API-KEY");
        log.info("API Key Filter checking request: {} with key: {}", request.getRequestURI(), requestKey);
        log.info("apiKey value  expected is :{}",apiKey);
        if (apiKey.equals(requestKey)) {
            filterChain.doFilter(request, response); // valid key, continue
        } else {
            log.warn("Invalid API key for path: {}", request.getRequestURI());

            ApiResponse<Void> apiResponse = new ApiResponse<>("Invalid API key", null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // exclude public endpoints if needed
        String path = request.getRequestURI();
        return path.startsWith("/public") || path.startsWith("/swagger");
    }
}

