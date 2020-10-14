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
import com.apollo.backend.model.Category;
import com.apollo.backend.repository.CategoryRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CategoryValidationTest extends GenericTest {

	@Autowired
	private CategoryRepository categoryRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Category newObject = categoryRepository.save(new Category("name"));

		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "name");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Category category = new Category("name");
		Map<String, Object> map = getMap(category);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostNameSize() throws Exception {
		Category category = new Category("");
		Map<String, Object> map = getMap(category);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPostNameNotNull() throws Exception {
		Category category = new Category(null);
		Map<String, Object> map = getMap(category);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		Category saved = categoryRepository.save(new Category("name"));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Category saved = categoryRepository.save(new Category("name"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "name");
		map.put("anotherProperty", "intruder");

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Category saved = categoryRepository.save(new Category("name"));
		
		Map<String, Object> map = getMap(saved);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutNameSize() throws Exception {
		Category category = categoryRepository.save(new Category("name"));
		category.setName("");

		Map<String, Object> map = getMap(category);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category/" + category.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name must be between 1 and 300 characters\" ] }"), mapper.readTree(response.getBody()));
	}

	@Test
	public void testPutNameNotNull() throws Exception {
		Category category = categoryRepository.save(new Category("name"));
		category.setName(null);
		Map<String, Object> map = getMap(category);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/category/" + category.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Name is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}
}