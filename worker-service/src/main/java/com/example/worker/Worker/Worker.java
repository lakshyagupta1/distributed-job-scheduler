package com.example.worker.Worker;

import com.example.worker.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Worker {

    private final String workerId = "Worker-" + System.currentTimeMillis();

    @Autowired
    private StringRedisTemplate redisTemplate;

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 3000)
    public void processJob() {

        String jobId = redisTemplate.opsForList().leftPop("jobQueue");

        if (jobId != null) {
            try {
                // Mark RUNNING
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/update/" + jobId + "/RUNNING",
                        null,
                        String.class
                );

                System.out.println(workerId + " processing job: " + jobId);

                // Fetch job details
                Job job = restTemplate.getForObject(
                        "http://localhost:8080/jobs/" + jobId,
                        Job.class
                );

                // Null safety
                if (job == null) {
                    System.out.println(workerId + " Job not found: " + jobId);
                    return;
                }

                String type = job.getJobType();
                String data = job.getData();

                if (type == null) {
                    System.out.println(workerId + " Job type is null for job: " + jobId);

                    throw new RuntimeException("Invalid job type");
                }

                System.out.println(workerId + " executing type: " + type);

                // Execute job
                switch (type) {

                    case "EMAIL":
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

                // Mark SUCCESS
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/update/" + jobId + "/SUCCESS",
                        null,
                        String.class
                );

                System.out.println(workerId + " completed job: " + jobId);

            } catch (Exception e) {

                System.out.println(workerId + " Error processing job: " + jobId);

                // Increase retry count
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/retry/" + jobId,
                        null,
                        String.class
                );

                // Get updated retry count
                Job job = restTemplate.getForObject(
                        "http://localhost:8080/jobs/" + jobId,
                        Job.class
                );

                int retryCount = job != null ? job.getRetryCount() : 0;

                if (retryCount < 3) {
                    redisTemplate.opsForList().rightPush("jobQueue", jobId);
                    System.out.println(workerId + " Retrying job: " + jobId + " count=" + retryCount);
                } else {
                    restTemplate.postForObject(
                            "http://localhost:8080/jobs/update/" + jobId + "/FAILED",
                            null,
                            String.class
                    );
                    System.out.println(workerId + " Job FAILED after retries: " + jobId);
                }
            }
        }
    }

    // Job types

    private void sendEmail(String email) {
        System.out.println(workerId + " Sending email to " + email);
        try { Thread.sleep(2000); } catch (Exception e) {}
        System.out.println(workerId + " Email sent to " + email);
    }

    private void processFile(String file) {
        System.out.println(workerId + " Processing file: " + file);
        try { Thread.sleep(3000); } catch (Exception e) {}
        System.out.println(workerId + " File processed: " + file);
    }

    private void logMessage(String msg) {
        System.out.println(workerId + " Log: " + msg);
    }
}