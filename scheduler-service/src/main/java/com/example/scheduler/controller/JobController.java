package com.example.scheduler.controller;

import com.example.scheduler.model.Job;
import com.example.scheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Submit job
    @PostMapping("/submit")
    public Job submitJob(@RequestBody Job job) {
        return jobService.submitJob(job);
    }

    // Get job status
    @GetMapping("/{id}")
    public Job getJob(@PathVariable Long id) {
        return jobService.getJob(id).orElseThrow();
    }

    // Update status (called by worker)
    @PostMapping("/update/{id}/{status}")
    public void updateStatus(@PathVariable Long id, @PathVariable String status) {
        jobService.updateStatus(id, status);
    }

    // Retry job
    @PostMapping("/retry/{id}")
    public void retryJob(@PathVariable Long id) {
        jobService.incrementRetry(id);
    }

    // Get all jobs (for dashboard)
    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }
}

