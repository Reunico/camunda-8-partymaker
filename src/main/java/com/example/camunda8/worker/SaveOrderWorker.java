package com.example.camunda8.worker;

import com.example.camunda8.constant.ProcessVariableConstant;
import com.example.camunda8.model.Order;
import com.example.camunda8.service.CrmService;
import com.example.camunda8.service.WebsiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaveOrderWorker {

  private final static Logger LOG = LoggerFactory.getLogger(SaveOrderWorker.class);

  private final CrmService crmService;

  @Autowired
  private ZeebeClient client;

  public SaveOrderWorker(WebsiteService websiteService, CrmService crmService) {
    this.crmService = crmService;
  }

  @JobWorker(type = "saveOrder")
  public void saveOrder(final ActivatedJob job) {
    LOG.warn("sh_zh before saveOrde_test : Send CRM with message content: ");
    ObjectMapper mapper = new ObjectMapper();
    Order order = mapper.convertValue(job.getVariable(ProcessVariableConstant.ORDER), Order.class);
    crmService.saveOrder(order);

    LOG.warn("sh_zh after : Send CRM : {}", order);
  }

}
