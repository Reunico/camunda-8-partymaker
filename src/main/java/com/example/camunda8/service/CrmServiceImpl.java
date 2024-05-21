package com.example.camunda8.service;

import com.example.camunda8.config.ApplicationProperties;
import com.example.camunda8.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrmServiceImpl implements CrmService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public CrmServiceImpl(RestTemplate restTemplate,
                          ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void saveOrder(Order order) {
        restTemplate.put(applicationProperties.getCrmUrl() + "/" + order.getId().toString(), order);
    }
}
