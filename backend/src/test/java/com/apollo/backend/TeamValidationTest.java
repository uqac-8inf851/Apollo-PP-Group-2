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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.model.Team;
import com.apollo.backend.repository.TeamRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TeamValidationTest extends GenericTest {

	@Autowired
	private TeamRepository teamRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Team newObject = teamRepository.save(new Team("name"));

		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "name");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Team team = new Team("name");
		Map<String, Object> map = getMap(team);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostNameSize() throws Exception {
		Team team = new Team("");
		Map<String, Object> map = getMap(team);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostNameNotNull() throws Exception {
		Team team = new Team(null);
		Map<String, Object> map = getMap(team);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		Team saved = teamRepository.save(new Team("name"));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Team saved = teamRepository.save(new Team("name"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "name");
		map.put("anotherProperty", "intruder");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Team saved = teamRepository.save(new Team("name"));
		
		Map<String, Object> map = getMap(saved);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutNameSize() throws Exception {
		Team team = teamRepository.save(new Team("name"));
		team.setName("");

		Map<String, Object> map = getMap(team);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team/" + team.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutNameNotNull() throws Exception {
		Team team = teamRepository.save(new Team("name"));
		team.setName(null);
		Map<String, Object> map = getMap(team);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/team/" + team.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}
}