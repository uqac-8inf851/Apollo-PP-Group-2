package com.apollo.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.TaskRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskValidationTest extends GenericTest {

	@Autowired
	private TaskRepository taskRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Task newObject = taskRepository.save(new Task("title", "description", 0));

		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "title");
		map.put("description", "description");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Task task = new Task("title", "description", 0);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostTitleSize() throws Exception {
		Task task = new Task("", "description", 0);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleNotNull() throws Exception {
		Task task = new Task(null, "description", 0);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleDescriptionSize() throws Exception {
		Task task = new Task("", "", 0);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}

	@Test
	public void testPostTitleDescriptionNotNull() throws Exception {
		Task task = new Task(null, null, 0);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		Task saved = taskRepository.save(new Task("title", "description", 0));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Task saved = taskRepository.save(new Task("title", "description", 0));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "title");
		map.put("description", "description");
		map.put("priority", 0);
		map.put("anotherProperty", "intruder");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Task saved = taskRepository.save(new Task("title", "description", 0));
		
		Map<String, Object> map = getMap(saved);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutTitleSize() throws Exception {
		Task task = taskRepository.save(new Task("title", "description", 0));
		task.setTitle("");

		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + task.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleNotNull() throws Exception {
		Task task = taskRepository.save(new Task("title", "description", 0));
		task.setTitle(null);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + task.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleDescriptionSize() throws Exception {
		Task task = taskRepository.save(new Task("title", "description", 0));
		task.setTitle("");
		task.setDescription("");

		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + task.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));

	}

	@Test
	public void testPutTitleDescriptionNotNull() throws Exception {
		Task task = taskRepository.save(new Task("title", "description", 0));
		task.setTitle(null);
		task.setDescription(null);
		Map<String, Object> map = getMap(task);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/task/" + task.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}
}