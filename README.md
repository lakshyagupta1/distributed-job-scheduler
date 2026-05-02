# \# Distributed Job Scheduler

# 

# \## Overview

# 

# This project is a simplified distributed job scheduler inspired by real-world systems like Kubernetes and background job processing frameworks.

# 

# It demonstrates how jobs can be processed asynchronously using multiple workers, a queue system, and basic scheduling mechanisms.

# 

# \---

# 

# \## Architecture

# 

# Client → Scheduler Service → Redis Queue → Worker Services → Execution → Database

# 

# \* \*\*Scheduler Service\*\*: Accepts jobs via API and stores them in MySQL

# \* \*\*Redis\*\*: Acts as a queue for decoupling and asynchronous processing

# \* \*\*Worker Services\*\*: Pick jobs from queues and execute them

# \* \*\*Database (MySQL)\*\*: Stores job details and status

# 

# \---

# 

# \## Features

# 

# \* Submit jobs via REST API

# \* Distributed workers process jobs in parallel

# \* Redis-based queue for asynchronous execution

# \* Multiple job types:

# 

# &#x20; \* EMAIL

# &#x20; \* FILE

# &#x20; \* LOG

# \* Retry mechanism for failed jobs

# \* Job status tracking (PENDING → RUNNING → SUCCESS / FAILED)

# \* Priority-based execution:

# 

# &#x20; \* HIGH

# &#x20; \* NORMAL

# &#x20; \* LOW

# \* Delayed job scheduling using `scheduledTime`

# \* Basic monitoring dashboard (HTML/JS)

# \* React UI (in progress)

# 

# \---

# 

# \## How It Works

# 

# 1\. User submits a job via API

# 2\. Scheduler stores job in MySQL

# 3\. Based on priority and time:

# 

# &#x20;  \* Immediate jobs → priority queues

# &#x20;  \* Future jobs → delayed queue

# 4\. Workers pick jobs from queues:

# 

# &#x20;  \* HIGH → NORMAL → LOW

# 5\. Worker executes job based on type

# 6\. Job status is updated

# 7\. Failed jobs are retried up to 3 times

# 

# \---

# 

# \## Job Structure

# 

# Example request:

# 

# ```json

# {

# &#x20; "jobType": "EMAIL",

# &#x20; "data": "user@gmail.com",

# &#x20; "priority": "HIGH",

# &#x20; "scheduledTime": 1715000000000

# }

# ```

# 

# \---

# 

# \## Tech Stack

# 

# \* Java (Spring Boot)

# \* Redis (Queue system)

# \* MySQL (Persistence)

# \* REST APIs

# \* HTML / JavaScript (basic UI)

# \* React (frontend improvement - ongoing)

# 

# \---

# 

# \## Why Redis?

# 

# Redis is used as an in-memory queue to:

# 

# \* decouple scheduler and workers

# \* enable asynchronous processing

# \* support horizontal scaling of workers

# \* provide fast push/pop operations

# 

# For larger-scale systems, tools like Kafka or RabbitMQ could be used.

# 

# \---

# 

# \## Project Structure

# 

# ```

# Distributed\_job\_scheduler/

# &#x20;├── scheduler-service/

# &#x20;├── worker-service/

# &#x20;├── frontend/ (React - in progress)

# &#x20;├── README.md

# ```

# 

# \---

# 

# \## Future Improvements

# 

# \* Use Redis Sorted Set (ZSET) for better scheduling

# \* Add cron-based scheduling

# \* Replace System.out logs with proper logging framework

# \* Improve UI with React dashboard

# \* Dockerize services

# \* Use Kafka for large-scale systems

# 

# \---

# 

# \## Author

# 

# Lakshya Gupta
# Diya Aggarwal



