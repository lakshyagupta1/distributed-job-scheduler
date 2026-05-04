package com.example.scheduler.model;

import jakarta.persistence.*;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType;      // EMAIL, FILE, LOG
    private String data;         // actual data
    private String status = "PENDING";
    private Long scheduledTime;  // timestamp (milliseconds)
    private String priority;     // HIGH, NORMAL, LOW
    private int retryCount = 0;

    // 🔥 NEW FIELD
    private String result;

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getJobType() {
        return jobType;
    }

    public String getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public Long getScheduledTime() {
        return scheduledTime;
    }

    public String getPriority() {
        return priority;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getResult() {
        return result;
    }

    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScheduledTime(Long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setResult(String result) {
        this.result = result;
    }
}