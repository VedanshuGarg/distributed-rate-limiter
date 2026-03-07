package com.vedanshu.distributedratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final int WINDOW_IN_SECONDS = 60;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String clientId) {
        String redisKey = "rate_limit:sliding:" + clientId;

        long currentTimeMs = Instant.now().toEpochMilli();
        long windowStartMs = currentTimeMs - (WINDOW_IN_SECONDS * 1000L);

        // 1. Clean up: Remove any timestamps that are older than our 60-second window
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStartMs);

        // 2. Count: How many requests has this user made in the current window?
        Long currentRequests = redisTemplate.opsForZSet().zCard(redisKey);

        // 3. Decide: Are they over the limit?
        if (currentRequests != null && currentRequests >= MAX_REQUESTS_PER_MINUTE) {
            System.out.println("🚨 BLOCKED: " + clientId + " exceeded sliding window limit!");
            return false;
        }

        // 4. Allowed! Add their new timestamp to the set
        // We use the timestamp as both the value (needs to be a string) and the score (needs to be a double)
        redisTemplate.opsForZSet().add(redisKey, String.valueOf(currentTimeMs), currentTimeMs);

        // 5. Memory Management: Set the whole key to expire after 60 seconds of inactivity
        // so we don't leak memory for users who leave the site.
        redisTemplate.expire(redisKey, Duration.ofSeconds(WINDOW_IN_SECONDS));

        System.out.println("ALLOWED: " + clientId + " (Request " + (currentRequests + 1) + " of " + MAX_REQUESTS_PER_MINUTE + ")");
        return true;
    }
}