# Distributed Job Scheduler

A distributed system built using Spring Boot, Redis, and MySQL to handle asynchronous job execution with retry mechanisms and load balancing.

## Features
- Job submission API
- Redis-based queue
- Multiple worker support
- Retry & failure handling
- Live dashboard

## Tech Stack
- Java (Spring Boot)
- MySQL
- Redis
- HTML + JS

## How to Run
1. Start MySQL & Redis
2. Run scheduler-service
3. Run worker-service (multiple instances supported)
4. Open http://localhost:8080
