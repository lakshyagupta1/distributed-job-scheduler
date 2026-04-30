package com.example.worker.model;

public class Job {

    private Long id;
    private String jobType;
    private String data;
    private String status;
    private int retryCount;

    public Job() {
    }

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
}