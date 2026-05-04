package com.example.worker.service;

import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

@Service
public class FileProcessingService {

    public Map<String, Object> processCsv(String filePath) throws Exception {

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Set<String> uniqueEmails = new HashSet<>();

        int valid = 0;
        int invalid = 0;
        int duplicates = 0;

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i);

            if (line.trim().isEmpty()) continue;

            String email = line.split(",")[0].trim();

            if (!isValidEmail(email)) {
                invalid++;
                continue;
            }

            if (uniqueEmails.contains(email)) {
                duplicates++;
                continue;
            }

            uniqueEmails.add(email);
            valid++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("valid", valid);
        result.put("invalid", invalid);
        result.put("duplicates", duplicates);
        result.put("emails", uniqueEmails);

        return result;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}