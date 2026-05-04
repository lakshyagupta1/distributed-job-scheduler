package com.example.worker.Worker;

import com.example.worker.model.Job;
import com.example.worker.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class Worker {

    private final String workerId = "Worker-" + System.currentTimeMillis();

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private EmailService emailService;

    // =========================
    // MAIN JOB PROCESSOR
    // =========================
    @Scheduled(fixedDelay = 100)
    public void processJob() {

        String jobId = fetchJobFromQueues();
        if (jobId == null) return;

        try {
            markStatus(jobId, "RUNNING");
            log("Processing job: " + jobId);

            Job job = fetchJob(jobId);
            if (job == null) {
                log("Job not found: " + jobId);
                return;
            }

            executeJob(job);

            markStatus(jobId, "SUCCESS");
            log("Completed job: " + jobId);

        } catch (Exception e) {
            handleFailure(jobId);
        }
    }

    // =========================
    // DELAYED JOB HANDLER
    // =========================
    @Scheduled(fixedDelay = 500)
    public void moveDelayedJobs() {

        List<String> jobs = redisTemplate.opsForList().range("delayedQueue", 0, -1);
        if (jobs == null || jobs.isEmpty()) return;

        for (String jobId : jobs) {

            Job job = fetchJob(jobId);
            if (job == null) continue;

            Long scheduledTime = job.getScheduledTime();

            if (scheduledTime == null || scheduledTime <= System.currentTimeMillis()) {
                redisTemplate.opsForList().remove("delayedQueue", 1, jobId);
                redisTemplate.opsForList().rightPush("jobQueue", jobId);
                log("Moved delayed job: " + jobId);
            }
        }
    }

    // =========================
    // HELPER METHODS
    // =========================

    private String fetchJobFromQueues() {
        String jobId = redisTemplate.opsForList().leftPop("highPriorityQueue");

        if (jobId == null) jobId = redisTemplate.opsForList().leftPop("jobQueue");
        if (jobId == null) jobId = redisTemplate.opsForList().leftPop("lowPriorityQueue");

        return jobId;
    }

    private Job fetchJob(String jobId) {
        return restTemplate.getForObject(
                "http://localhost:8080/jobs/" + jobId,
                Job.class
        );
    }

    private void markStatus(String jobId, String status) {
        restTemplate.postForObject(
                "http://localhost:8080/jobs/update/" + jobId + "/" + status,
                null,
                String.class
        );
    }

    private void executeJob(Job job) {

        String type = job.getJobType();
        String data = job.getData();

        if (type == null || type.trim().isEmpty()) {
            throw new RuntimeException("Invalid job type");
        }

        log("Executing type: " + type);

        switch (type.toUpperCase()) {

            case "EMAIL":
                //emailService.sendEmail(data);
                sendEmail(data);
                break;

            case "FILE":
                processFile(data);
                break;

            case "LOG":
                logMessage(data);
                break;

            default:
                throw new RuntimeException("Unknown job type: " + type);
        }
    }

    private void handleFailure(String jobId) {

        log("Error processing job: " + jobId);

        // Increase retry count
        restTemplate.postForObject(
                "http://localhost:8080/jobs/retry/" + jobId,
                null,
                String.class
        );

        Job job = fetchJob(jobId);
        int retryCount = job != null ? job.getRetryCount() : 0;

        if (retryCount < 3) {
            redisTemplate.opsForList().rightPush("jobQueue", jobId);
            log("Retrying job: " + jobId + " count=" + retryCount);
        } else {
            markStatus(jobId, "FAILED");
            log("Job FAILED after retries: " + jobId);
        }
    }

    private void log(String message) {
        System.out.println(workerId + " | " + message);
    }

    // =========================
    // JOB IMPLEMENTATIONS
    // =========================
    //No usage Right now
    private void sendEmail(String email) {
        log("Sending email to " + email);
        sleep(500);
        log("Email sent to " + email);
    }

    private void processFile(String file) {
        log("Processing file: " + file);
        sleep(3000);
        log("File processed: " + file);
    }

    private void logMessage(String msg) {
        sleep(3000);
        log("Log: " + msg);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}