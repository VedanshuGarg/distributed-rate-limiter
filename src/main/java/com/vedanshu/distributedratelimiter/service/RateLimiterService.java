package com.vedanshu.distributedratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientId, int maxRequests, int windowInSeconds) {
        String redisKey = "rate_limit:sliding:" + clientId;

        long currentTimeMs = Instant.now().toEpochMilli();
        long windowStartMs = currentTimeMs - (windowInSeconds * 1000L);

        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStartMs);
        Long currentRequests = redisTemplate.opsForZSet().zCard(redisKey);

        if (currentRequests != null && currentRequests >= maxRequests) {
            System.out.println("BLOCKED: " + clientId + " exceeded sliding window limit!");
            return false;
        }

        redisTemplate.opsForZSet().add(redisKey, String.valueOf(currentTimeMs), currentTimeMs);
        redisTemplate.expire(redisKey, Duration.ofSeconds(windowInSeconds));

        System.out.println("ALLOWED: " + clientId + " (Request " + (currentRequests + 1) + " of " + maxRequests + ")");
        return true;
    }
}
