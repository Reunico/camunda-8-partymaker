package com.example.camunda8;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@Deployment(resources = "classpath:azhukov_8_partymaker.bpmn")
public class Camunda8Application implements CommandLineRunner {

	private final static Logger LOG = LoggerFactory.getLogger(Camunda8Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Camunda8Application.class, args);
	}

	@Autowired
	private ZeebeClient client;

	@Override
	public void run(String... args) throws Exception {
/*		final ProcessInstanceEvent event =
				client
						.newCreateInstanceCommand()
						.bpmnProcessId("azhukov_8_partymaker")
						.latestVersion()
						.variables(Map.of("message_content", "Hello from the Spring Boot get started"))
						.send()
						.join();

		LOG.warn("sh_zh Started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
				event.getProcessDefinitionKey(), event.getBpmnProcessId(), event.getVersion(), event.getProcessInstanceKey());
*/
	}
}
