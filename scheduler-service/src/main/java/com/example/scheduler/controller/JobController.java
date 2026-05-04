package com.example.scheduler.controller;

import com.example.scheduler.model.Job;
import com.example.scheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
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

    @PostMapping("/updateResult/{id}")
    public void updateResult(@PathVariable Long id, @RequestParam String result) {
        jobService.updateResult(id, result);
    }

    //Bulk Jobs Upload
    @PostMapping("/upload")
    public Map<String, Object> uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
        return jobService.processCsv(file);
    }
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create upload directory");
            }
        }

        String filePath = uploadDir + file.getOriginalFilename();

        File dest = new File(filePath);
        file.transferTo(dest);

        Job job = new Job();
        job.setJobType("FILE");
        job.setData(filePath);
        job.setPriority("NORMAL");

        jobService.submitJob(job);

        return "File job submitted!";
    }
}

