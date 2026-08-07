package org.example.taskflow.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public String getStatus() {
        return "TaskFlow is running";
    }
}
