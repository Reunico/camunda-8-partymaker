package com.example.camunda8;

import com.example.camunda8.model.Order;
import com.example.camunda8.service.CrmService;
import com.example.camunda8.service.OrderService;
import com.example.camunda8.service.WebsiteService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivateJobsResponse;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.RecordStreamSource;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ZeebeProcessTest
@SpringBootTest
@Slf4j
class Camunda8ApplicationTests {

	private ZeebeClient zeebe;

	private ZeebeTestEngine engine;

	private RecordStreamSource recordStreamSource;

	@Mock
	WebsiteService websiteServiceMock = mock();

	@Mock
	CrmService crmServiceMock = mock();

	@Mock
	OrderService orderServiceMock = mock();

	@BeforeEach
	public void init(){
		DeploymentEvent deploymentEvent = zeebe.newDeployResourceCommand()
				.addResourceFromClasspath("./process/azhukov_8_partymaker.bpmn")
				.addResourceFromClasspath("./process/azhukov_8_proposal.dmn")
				.addResourceFromClasspath("./process/enter-email-message.form")
				.addResourceFromClasspath("./process/azhukov_8_approveCRM.form")
				.send()
				.join();
		assertThat(deploymentEvent).containsProcessesByBpmnProcessId("azhukov_8_partymaker");

		Mockito.when(websiteServiceMock.getOrder()).thenReturn(getOrder());
		Mockito.doNothing().when(crmServiceMock).saveOrder(any());
		Mockito.when(orderServiceMock.persistOrder(any(),any(),any(),any(),any(),any(),any())).thenReturn(getOrder());
	}

	public Order getOrder(){
		Order order = new Order();
		order.setAmount(new BigDecimal(200));
		order.setId(UUID.randomUUID());
		order.setDescription("test description");
		//order.setOrderDate(LocalDate.from(LocalDate.now().plusDays(11L).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		order.setTitle("title");
		order.setFullName("Full Name");
		order.setContractor("Contractor");
		return order;
	}

	@Test
	void contextLoads() throws Exception {

		ProcessInstanceEvent processInstance =
				startInstance("azhukov_8_partymaker");


		Map<String, Object> var;
		var = Map.of("message_content", "Test Hello from the Spring Boot get started");
		waitForUserTaskAndComplete("enterMessageTask", var);

		log.warn("Test sh -- Passed Element: {}", "Activity_getOrder");

		completeJob("getOrder");

		log.warn("Test sh -- hasVariable( order ): ");

		var = Map.of("approved", "yes");
		waitForUserTaskAndComplete("approveOrder", var);

		log.warn("Test sh -- UserTask complete ApproveOrder: ");

		log.warn("Test sh -- Gateway complete Gateway_0mz4rbf: ");

		completeJob("persistOrder");

		log.warn("Test sh -- persistOrder: ");

		var = Map.of("approved", "yes");

		waitForUserTaskAndComplete("Activity_0xl92h5", var);

		completeJob("saveOrder");

		assertThat(processInstance)
				.hasVariable("order")
				.hasPassedElement("enterMessageTask")
				.hasNotPassedElement("Activity_manual")
				.isCompleted();

		log.warn("Test sh -- isComplete: ");
	}
	public ProcessInstanceEvent startInstance(String id){
		ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
				.bpmnProcessId(id)
				.latestVersion()
				.send().join();
		assertThat(processInstance).isStarted();
		return processInstance;
	}
	public void waitForUserTaskAndComplete(String userTaskId, Map<String, Object> variables){
		// Получаем все пользовательские таски
		List<ActivatedJob> jobs = zeebe.newActivateJobsCommand().jobType(USER_TASK_JOB_TYPE).maxJobsToActivate(1).send().join().getJobs();
		assertTrue("Job for user task enterMessageTask does not exist", jobs.size()>0);
		ActivatedJob userTaskJob = jobs.get(0);
		assertEquals(userTaskId, userTaskJob.getElementId());
		log.warn("Test sh -- User task found: {}", userTaskJob.getElementId());
		log.warn("Test sh variables -- User task found: {}", variables);

		if (variables!=null) {
			zeebe.newCompleteCommand(userTaskJob.getKey()).variables(variables).send().join();
		} else {
			zeebe.newCompleteCommand(userTaskJob.getKey()).send().join();
		}
	}

	public void completeJob(String type) throws Exception {
		ActivateJobsResponse response= zeebe.newActivateJobsCommand()
				.jobType(type)
				.maxJobsToActivate(1)
				.send()
				.join();

		ActivatedJob activatedJob = response.getJobs().get(0);
		BpmnAssert.assertThat(activatedJob);

		zeebe.newCompleteCommand(activatedJob.getKey()).send().join();

		engine.waitForIdleState(Duration.ofSeconds(1));
	}

}
