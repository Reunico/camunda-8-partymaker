package com.example.camunda8.worker;

import com.example.camunda8.constant.ProcessVariableConstant;
import com.example.camunda8.model.Order;
import com.example.camunda8.service.OrderService;
import com.fasterxml.jackson.core.JsonParser;
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
public class PersistOrderWorker {

  private final static Logger LOG = LoggerFactory.getLogger(PersistOrderWorker.class);

  @Autowired
  private ZeebeClient client;

  private final OrderService orderService;

  public PersistOrderWorker(OrderService orderService) {
    this.orderService = orderService;
  }

  @JobWorker(type = "persistOrder")
  public void persistOrderWorker(final ActivatedJob job) throws Exception {
    LOG.warn("sh_zh before persistOrder_test : Persist Order content: ");

    ObjectMapper mapper = new ObjectMapper();
    Order order = mapper.convertValue(job.getVariable(ProcessVariableConstant.ORDER), Order.class);
    LOG.warn("sh_zh job.getVariablesAsType persistOrder_test : {}", order);
    Order orderPersist = orderService.persistOrder( order,
            order.getDescription(),
            order.getContractor(),
            order.getOrderDate(),
            order.getFullName(),
            order.getTitle(),
            order.getAmount().longValue()
    );

            client
                    .newSetVariablesCommand(job.getElementInstanceKey())
                    .variables(Map.of(ProcessVariableConstant.ORDER, orderPersist))
                    .send()
                    .join();

    LOG.warn("sh_zh after : Set persistOrder variables with content: {}", orderPersist);
  }

}
