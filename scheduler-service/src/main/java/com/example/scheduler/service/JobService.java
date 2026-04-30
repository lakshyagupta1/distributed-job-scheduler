package com.example.scheduler.service;

import com.example.scheduler.model.Job;
import com.example.scheduler.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
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

        // push to Redis queue
        redisTemplate.opsForList()
                .rightPush("jobQueue", job.getId().toString());

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
}