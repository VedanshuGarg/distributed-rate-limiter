# 🛡️ Distributed API Rate Limiter

A scalable, distributed rate-limiting middleware built with **Java 17**, **Spring Boot 3**, and **Redis**. Designed to protect APIs from abuse, brute-force attacks, and traffic spikes using the **Token Bucket** algorithm.

![Java](https://img.shields.io/badge/Java-17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## 🏗️ Architecture Overview

*(Architecture diagram and details coming soon...)*

This project implements rate limiting in a distributed environment. By utilizing **Redis** as a centralized, in-memory data store, multiple instances of this Spring Boot application can share rate-limit counters in real-time, ensuring consistency across a microservices cluster.

## 🚀 Key Features (In Development)
- **Token Bucket Algorithm:** Smooths out bursty traffic while enforcing strict upper limits.
- **Distributed State:** Uses Redis to share request counts across multiple application nodes.
- **Low Latency:** Millisecond-level decision making to prevent API bottlenecking.
- **Customizable Limits:** Configurable thresholds based on IP Address or API Key.

---
*Built by Vedanshu Garg | Associate Software Engineer @ Cloudsufi*
