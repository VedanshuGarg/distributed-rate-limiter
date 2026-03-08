package com.vedanshu.distributedratelimiter.aspect;

import com.vedanshu.distributedratelimiter.annotation.RateLimit;
import com.vedanshu.distributedratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class RateLimitAspect {

    private final RateLimiterService rateLimiterService;

    public RateLimitAspect(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Around("@annotation(rateLimitAnnotation)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimitAnnotation) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String clientIp = request.getRemoteAddr();

        int maxRequests = rateLimitAnnotation.maxRequests();
        int window = rateLimitAnnotation.windowInSeconds();

        boolean isAllowed = rateLimiterService.isAllowed(clientIp, maxRequests, window);

        if (!isAllowed) {
            // If blocked, immediately throw a 429 Too Many Requests error.
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate Limit Exceeded. Try again later.");
        }

        return joinPoint.proceed();
    }
}
