package com.example.camunda8.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Override
    public void notify(String taskId, String taskName, String message) {
        log.warn("Notify Service: {}, {}, {}", taskId, taskName, message);
    }
}
