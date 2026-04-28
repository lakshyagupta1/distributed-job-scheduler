package com.example.worker.Worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.worker.model.Job;

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
                // 🔹 Mark RUNNING
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/update/" + jobId + "/RUNNING",
                        null,
                        String.class
                );

                System.out.println(workerId + " processing job: " + jobId);
                //if (true) throw new RuntimeException();//to test faults and retries
                Thread.sleep(3000);

                // 🔹 Mark SUCCESS
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/update/" + jobId + "/SUCCESS",
                        null,
                        String.class
                );

                System.out.println(workerId + " completed job: " + jobId);

            } catch (Exception e) {

                // 🔹 Increase retry count
                restTemplate.postForObject(
                        "http://localhost:8080/jobs/retry/" + jobId,
                        null,
                        String.class
                );

                // 🔹 Get updated job
                Job job = restTemplate.getForObject(
                        "http://localhost:8080/jobs/" + jobId,
                        Job.class
                );

                int retryCount = job.getRetryCount();

                if (retryCount < 3) {
                    // retry again
                    redisTemplate.opsForList().rightPush("jobQueue", jobId);
                    System.out.println(workerId +"Retrying job: " + jobId + " count=" + retryCount);
                } else {
                    // mark failed
                    restTemplate.postForObject(
                            "http://localhost:8080/jobs/update/" + jobId + "/FAILED",
                            null,
                            String.class
                    );
                    System.out.println(workerId +" Job FAILED after retries: " + jobId);
                }
            }
        }
    }
}