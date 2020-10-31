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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectValidationTest extends GenericTest {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProgramRepository programRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project newObject = projectRepository.save(new Project("title", "description", program));

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
		Program program = programRepository.save(new Program("title", "description"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectTitle", "title");
		map.put("projectDescription", "description");
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostTitleSize() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("", "description", program);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleNotNull() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project(null, "description", program);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostTitleDescriptionSize() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("", "", program);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

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
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project(null, null, program);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

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
		Program program = programRepository.save(new Program("title", "description"));

		Project saved = projectRepository.save(new Project("title", "description", program));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project saved = projectRepository.save(new Project("title", "description", program));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectTitle", "title");
		map.put("projectDescription", "description");
		map.put("anotherProperty", "intruder");
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project saved = projectRepository.save(new Project("title", "description", program));
		
		Map<String, Object> map = getMap(saved);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutTitleSize() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		project.setProjectTitle("");

		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleNotNull() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		project.setProjectTitle(null);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Title is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutTitleDescriptionSize() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		project.setProjectTitle("");
		project.setProjectDescription("");

		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

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
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		project.setProjectTitle(null);
		project.setProjectDescription(null);
		Map<String, Object> map = getMap(project);
		map.put("program", getUrl() + "/program/" + program.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/project/" + project.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();

		Content expected = mapper.readValue("{\"content\" : [ \"Title is mandatory\", \"Description is mandatory\" ] }", Content.class);
		Content returned = mapper.readValue(response.getBody(), Content.class);

		assertTrue(expected.containsEquals(returned));
	}
}