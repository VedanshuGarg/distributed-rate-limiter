# 🛡️ Distributed API Rate Limiter

A high-performance, distributed rate-limiting middleware built with **Java 17**, **Spring Boot 3**, and **Redis**. Designed to protect APIs from abuse, brute-force attacks, and traffic spikes using the **Sliding Window Log** algorithm and **Spring AOP**.

![Java](https://img.shields.io/badge/Java-17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## 🏗️ Architecture & Algorithms

### 1. The Sliding Window Log (Redis ZSET)
Standard Fixed Window algorithms suffer from the "Burst Problem," allowing users to double their traffic at the edge of a time window. This project solves that by implementing a **Sliding Window Log** using Redis Sorted Sets (`ZSET`).



* Every request is logged in Redis with its exact millisecond timestamp as the score.
* Older timestamps outside the current window are actively pruned.
* This ensures pixel-perfect limits over any rolling time window and eliminates edge-case bursting.

### 2. Distributed State via Redis
By utilizing an in-memory Redis cluster rather than local JVM memory, this rate limiter works seamlessly across multiple distributed instances of the Spring Boot application. All nodes share the exact same state and counters in real-time.

### 3. Decoupled Logic (Spring AOP)
Instead of hardcoding rate-limiting logic into individual controllers, this project utilizes Aspect-Oriented Programming (AOP). 



By creating a custom `@RateLimit` annotation, the rate limiter acts as an invisible interceptor. It evaluates the request IP and Redis state, dynamically throwing a `429 Too Many Requests` error before the business logic is ever touched.

---

## 💻 Usage & Implementation

Protecting an endpoint is as simple as adding the `@RateLimit` annotation. You can customize the `maxRequests` and `windowInSeconds` per endpoint.

```java
@RestController
@RequestMapping("/api")
public class ApiController {

    // Strict Limit: 3 requests per 30 seconds
    @GetMapping("/data")
    @RateLimit(maxRequests = 3, windowInSeconds = 30)
    public String getSecureData() {
        return "Success! Here is your secure data.";
    }
    
    // Heavy Endpoint Limit: 1 request per 60 seconds
    @GetMapping("/heavy-data")
    @RateLimit(maxRequests = 1, windowInSeconds = 60)
    public String getHeavyData() {
        return "Success! Here is your massive payload.";
    }
}
```


🚀 Quick Start

Prerequisites
```bash
Java 17+
Docker & Docker Compose
Maven
```

1. Start the Redis Brain
Use the included docker-compose.yml to spin up a local Redis instance on port 6379.
```bash
docker-compose up -d
```

2. Run the Application
```bash
./mvnw spring-boot:run
```

3. Test the Shield
Hit the endpoint repeatedly to watch the Aspect drop the hammer with a 429 Too Many Requests status code.
```bash
curl -v http://localhost:8080/api/data
```

Built by Vedanshu Garg | Associate Software Engineer @ Cloudsufi
