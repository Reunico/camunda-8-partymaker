package com.example.camunda8.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
@Setter
@Getter
public class ApplicationProperties {
    private String crmUrl;
    private String websiteUrl;
}
