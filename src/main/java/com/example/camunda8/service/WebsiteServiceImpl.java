package com.example.camunda8.service;

import com.example.camunda8.config.ApplicationProperties;
import com.example.camunda8.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public WebsiteServiceImpl(RestTemplate restTemplate,
                              ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Order getOrder() {
        return restTemplate.getForObject(applicationProperties.getWebsiteUrl(), Order.class);
    }
}
