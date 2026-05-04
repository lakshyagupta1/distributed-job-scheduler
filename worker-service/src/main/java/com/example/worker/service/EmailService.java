package com.example.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RestTemplate restTemplate;
    public void createEmailJob(String email) {

        Map<String, Object> req = new HashMap<>();
        req.put("jobType", "EMAIL");
        req.put("data", email);
        req.put("priority", "NORMAL");

        restTemplate.postForObject(
                "http://localhost:8080/jobs/submit",
                req,
                String.class
        );
    }

    public void sendEmail(String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Job Executed");
        message.setText("Your job has been successfully executed!");

        mailSender.send(message);
    }
}