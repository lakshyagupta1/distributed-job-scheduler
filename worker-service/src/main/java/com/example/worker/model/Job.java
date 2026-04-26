package com.example.worker.model;

public class Job {
    private Long id;
    private String payload;
    private String status;
    private int retryCount;

    public int getRetryCount() {
        return retryCount;
    }
}