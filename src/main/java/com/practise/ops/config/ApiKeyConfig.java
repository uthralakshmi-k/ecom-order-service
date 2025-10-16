//package com.practise.ops.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ApiKeyConfig {
//
//    // If api.key is missing, use "default-api-key"
//    @Value("${app.api.key}")
//    private String appApiKey;
//
//    public String getApiKey() {
//        return appApiKey;
//    }
//
//    @PostConstruct
//    public void init() {
//        System.out.println("Injected API Key: " + appApiKey);
//    }
//}
