package com.example.camunda8.worker;

import com.example.camunda8.constant.BpmnErrorMessage;
import com.example.camunda8.constant.ProcessVariableConstant;
import com.example.camunda8.model.Order;
import com.example.camunda8.service.WebsiteService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetOrderWorker {

  private final static Logger LOG = LoggerFactory.getLogger(GetOrderWorker.class);

  private final WebsiteService websiteService;

  @Autowired
  private ZeebeClient client;

  public GetOrderWorker(WebsiteService websiteService) {
    this.websiteService = websiteService;
  }

  @JobWorker(type = "getOrder")
  public void getOrderfromSite(final ActivatedJob job) {
    LOG.warn("sh_zh before getOrde_test : Sending email with message content: ");

    try{


      Order order = websiteService.getOrder();
      client
              .newSetVariablesCommand(job.getElementInstanceKey())
              .variables(Map.of(ProcessVariableConstant.ORDER, order))
              .send()
              .join();

      LOG.warn("sh_zh after : Set variables with content: {}", order);

    } catch (Exception e){
      client.newThrowErrorCommand(job.getKey())
              .errorCode(BpmnErrorMessage.GET_ORDER_ERROR)
              .errorMessage("Something bad happened and it was your fault")
              .send()
              .exceptionally((throwable -> {
                throw new RuntimeException("Could not throw the BPMN Error Event", throwable);
              }));

      LOG.warn("sh_zh getOrder Error : {}",  e.getMessage());
    }

  }

}
