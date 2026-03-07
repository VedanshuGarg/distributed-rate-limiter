package com.vedanshu.distributedratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final int WINDOW_IN_SECONDS = 60;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Checks if a client is allowed to make a request.
     * @param clientId The IP address or API Key of the user
     * @return true if allowed, false if blocked (Rate Limited)
     */
    public boolean isAllowed(String clientId) {
        String redisKey = "rate_limit:" + clientId;

        Long currentRequests = redisTemplate.opsForValue().increment(redisKey);

        if (currentRequests != null && currentRequests == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(WINDOW_IN_SECONDS));
        }

        if (currentRequests != null && currentRequests > MAX_REQUESTS_PER_MINUTE) {
            System.out.println("BLOCKED: " + clientId + " exceeded rate limit!");
            return false;
        }

        System.out.println("ALLOWED: " + clientId + " (Request " + currentRequests + " of " + MAX_REQUESTS_PER_MINUTE + ")");
        return true;
    }
}
