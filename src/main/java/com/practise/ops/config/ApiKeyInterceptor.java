//package com.practise.ops.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ApiKeyInterceptor implements HandlerInterceptor {
//
//   // private final ApiKeyConfig apiKeyConfig;
//
////    @Override
////    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
////            throws Exception {
////
////        String requestApiKey = request.getHeader("API-KEY");
////System.out.println("requestApiKey ="+requestApiKey);
////        if (requestApiKey == null || !requestApiKey.equals(apiKeyConfig.getApiKey())) {
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            response.getWriter().write("{\"message\": \"Invalid or missing API key\"}");
////            return false;
////        }
////
////        return true; // valid key → continue to controller
////    }
//
//    @Value("${app.api.key}")
//    private String apiKey;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//       // String requestApiKey = request.getHeader("X-API-KEY");
//
//        String rApiKey = request.getHeader("API-KEY");
//
//        log.info("Intercepted request for {} with API key: {}", request.getRequestURI(), rApiKey);
//        if (rApiKey == null || !rApiKey.equals(apiKey)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"message\": \"Invalid API key\", \"data\": null, \"status\": 401}");
//            return false;
//        }
//
//
//
//        return true; // continue to controller
//    }
//    @PostConstruct
//    public void init() {
//        log.info("✅ ApiKeyInterceptor initialized with key: {}", apiKey);
//    }
//}
