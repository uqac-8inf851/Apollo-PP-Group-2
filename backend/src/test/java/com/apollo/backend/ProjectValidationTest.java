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
import com.apollo.backend.model.Project;
import com.apollo.backend.repository.ProjectRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectValidationTest extends GenericTest {

	@Autowired
	private ProjectRepository projectRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Project newObject = projectRepository.save(new Project("title", "description"));

		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "title");
		map.put("description", "description");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Project project = new Project("title", "description");
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostTitleSize() throws Exception {
		Project project = new Project("", "description");
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleNotNull() throws Exception {
		Project project = new Project(null, "description");
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleDescriptionSize() throws Exception {
		Project project = new Project("", "");
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}

	@Test
	public void testPostTitleDescriptionNotNull() throws Exception {
		Project project = new Project(null, null);
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		Project saved = projectRepository.save(new Project("title", "description"));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Project saved = projectRepository.save(new Project("title", "description"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "title");
		map.put("description", "description");
		map.put("anotherProperty", "intruder");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Project saved = projectRepository.save(new Project("title", "description"));
		
		Map<String, Object> map = getMap(saved);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutTitleSize() throws Exception {
		Project project = projectRepository.save(new Project("title", "description"));
		project.setTitle("");

		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleNotNull() throws Exception {
		Project project = projectRepository.save(new Project("title", "description"));
		project.setTitle(null);
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleDescriptionSize() throws Exception {
		Project project = projectRepository.save(new Project("title", "description"));
		project.setTitle("");
		project.setDescription("");

		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title must be between 1 and 300 characters\", \"Description must be between 1 and 300 characters\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));

	}

	@Test
	public void testPutTitleDescriptionNotNull() throws Exception {
		Project project = projectRepository.save(new Project("title", "description"));
		project.setTitle(null);
		project.setDescription(null);
		Map<String, Object> map = getMap(project);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}
}