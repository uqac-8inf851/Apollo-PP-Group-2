package com.apollo.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.TaskRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskValidationTest extends GenericTest {

	@Autowired
	private TaskRepository taskRepository;

    @Autowired
    private ApplicationContext applicationContext;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	private ResponseEntity<String> doPost(Map<String, Object> map) {
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		return restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);
	}

	private ResponseEntity<String> doPut(Map<String, Object> map, Integer id) {
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		return restTemplate.exchange(getUrl() + "/task/" + id, HttpMethod.PUT, httpEntity, String.class);
	}

	@Test
	public void testSave() throws Exception {

		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task newObject = taskRepository.save(taskBuild.getTask());
		
		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskTitle", "title");
		map.put("taskDescription", "description");
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());
		map.put("category", getUrl() + "/category/" + taskBuild.getCategory().getId());
		map.put("status", getUrl() + "/status/" + taskBuild.getStatus().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Map<String, Object> map = getMap(taskBuild.getTask());
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());
		map.put("category", getUrl() + "/category/" + taskBuild.getCategory().getId());
		map.put("status", getUrl() + "/status/" + taskBuild.getStatus().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostTitleSize() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "", "description");

		Map<String, Object> map = getMap(taskBuild.getTask());
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleNotNull() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, null, "description");
		
		Map<String, Object> map = getMap(taskBuild.getTask());
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleDescriptionSize() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "", "");

		Map<String, Object> map = getMap(taskBuild.getTask());
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}

	@Test
	public void testPostTitleDescriptionNotNull() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, null, null);

		Map<String, Object> map = getMap(taskBuild.getTask());
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPost(map);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task saved = taskRepository.save(taskBuild.getTask());

		Map<String, Object> map = new HashMap<String, Object>();

		ResponseEntity<String> response = doPut(map, saved.getId());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task saved = taskRepository.save(taskBuild.getTask());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskTitle", "title");
		map.put("taskDescription", "description");
		map.put("priority", 0);
		map.put("anotherProperty", "intruder");
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());
		map.put("category", getUrl() + "/category/" + taskBuild.getCategory().getId());
		map.put("status", getUrl() + "/status/" + taskBuild.getStatus().getId());

		ResponseEntity<String> response = doPut(map, saved.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task saved = taskRepository.save(taskBuild.getTask());
		
		Map<String, Object> map = getMap(saved);
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());
		map.put("category", getUrl() + "/category/" + taskBuild.getCategory().getId());
		map.put("status", getUrl() + "/status/" + taskBuild.getStatus().getId());

		ResponseEntity<String> response = doPut(map, saved.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutTitleSize() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task task = taskRepository.save(taskBuild.getTask());
		task.setTaskTitle("");

		Map<String, Object> map = getMap(task);
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPut(map, task.getId());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleNotNull() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task task = taskRepository.save(taskBuild.getTask());
		task.setTaskTitle(null);
		Map<String, Object> map = getMap(task);
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPut(map, task.getId());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleDescriptionSize() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task task = taskRepository.save(taskBuild.getTask());
		task.setTaskTitle("");
		task.setTaskDescription("");

		Map<String, Object> map = getMap(task);
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPut(map, task.getId());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));

	}

	@Test
	public void testPutTitleDescriptionNotNull() throws Exception {
		TaskBuilder taskBuild = applicationContext.getBean(TaskBuilder.class, "title", "description");

		Task task = taskRepository.save(taskBuild.getTask());
		task.setTaskTitle(null);
		task.setTaskDescription(null);
		Map<String, Object> map = getMap(task);
		map.put("project", getUrl() + "/project/" + taskBuild.getProject().getId());

		ResponseEntity<String> response = doPut(map, task.getId());

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}
}