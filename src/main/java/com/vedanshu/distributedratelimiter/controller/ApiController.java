package com.vedanshu.distributedratelimiter.controller;

import com.vedanshu.distributedratelimiter.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/data")
    @RateLimit(maxRequests = 3, windowInSeconds = 30)
    public String getSecureData() {
        return "Success! Here is your secure data.\n";
    }

    @GetMapping("/heavy-data")
    @RateLimit(maxRequests = 1, windowInSeconds = 60)
    public String getHeavyData() {
        return "Success! Here is your massive payload.\n";
    }
}
