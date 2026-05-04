package com.example.scheduler.service;

import com.example.scheduler.model.Job;
import com.example.scheduler.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Job submitJob(Job job) {

        // default values
        job.setStatus("PENDING");
        job.setRetryCount(0);

        // save to DB
        job = jobRepository.save(job);

        String jobId = job.getId().toString();

        Long now = System.currentTimeMillis();

        // 🔹 1. Check if job is scheduled for future
        if (job.getScheduledTime() != null && job.getScheduledTime() > now) {

            redisTemplate.opsForList()
                    .rightPush("delayedQueue", jobId);

        } else {

            // 🔹 2. Handle priority
            String priority = job.getPriority();

            if ("HIGH".equalsIgnoreCase(priority)) {

                redisTemplate.opsForList()
                        .rightPush("highPriorityQueue", jobId);

            } else if ("LOW".equalsIgnoreCase(priority)) {

                redisTemplate.opsForList()
                        .rightPush("lowPriorityQueue", jobId);

            } else {

                // default = NORMAL
                redisTemplate.opsForList()
                        .rightPush("jobQueue", jobId);
            }
        }

        return job;
    }

    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    public void updateStatus(Long id, String status) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setStatus(status);
        jobRepository.save(job);
    }
    public void incrementRetry(Long id) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setRetryCount(job.getRetryCount() + 1);
        jobRepository.save(job);
    }
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Map<String, Object> processCsv(MultipartFile file) throws Exception {

        List<String> lines = new BufferedReader(
                new InputStreamReader(file.getInputStream()))
                .lines()
                .skip(1) // skip header
                .toList();

        int success = 0;
        int failed = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            try {
                String[] parts = line.split(",");

                String email = parts[0].trim();
                String priority = parts[1].trim();

                if (!isValidEmail(email)) {
                    failed++;
                    continue;
                }

                Job job = new Job();
                job.setJobType("EMAIL");
                job.setData(email);
                job.setPriority(priority);

                submitJob(job);
                success++;

            } catch (Exception e) {
                failed++;
                System.out.println("Error processing line: " + line);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("failed", failed);
        result.put("total", success + failed);;

        return result;
    }
    public void updateResult(Long id, String result) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setResult(result);
        jobRepository.save(job);
    }
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}