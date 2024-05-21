package com.example.camunda8.service;

public interface NotifyService {
    void notify(String taskId, String taskName, String message);
}
