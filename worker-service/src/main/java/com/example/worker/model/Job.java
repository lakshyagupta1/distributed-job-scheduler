package com.example.worker.model;

public class Job {

    private Long id;
    private String jobType;
    private String data;
    private String status;
    private int retryCount;
    private Long scheduledTime;
    private String priority;

    public Job() {
    }

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

    public int getRetryCount() {
        return retryCount;
    }

    public Long getScheduledTime() {
        return scheduledTime;
    }

    public String getPriority() {
        return priority;
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

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setScheduledTime(Long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}