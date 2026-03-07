package com.vedanshu.distributedratelimiter.controller;

import com.vedanshu.distributedratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RateLimiterService rateLimiterService;

    public ApiController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/data")
    public ResponseEntity<String> getSecureData(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();

        boolean isAllowed = rateLimiterService.isAllowed(clientIp);

        if (!isAllowed) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate Limit Exceeded. Try again in a minute.\n");
        }

        return ResponseEntity.ok("Success! Here is your secure data.\n");
    }
}
