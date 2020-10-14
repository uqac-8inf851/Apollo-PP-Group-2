package com.apollo.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Map;

import com.apollo.backend.model.Category;
import com.apollo.backend.repository.CategoryRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CategoryTest extends GenericTest {

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
	public void saveNewCategoryTest() throws Exception {
		Category category = new Category("name");

		Category response = categoryRepository.save(category);

		assertNotNull(response);
	}

	@Test
	public void postNewCategoryTest() throws Exception {
		Category category = new Category("name");

		Map<String, Object> categoryMap = getMap(category);

		ResponseEntity<Category> response = restTemplate.postForEntity(getUrl() + "/category", categoryMap, Category.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllCategoryForEntitiesTest() throws Exception {
		Category category = new Category("name");

		Map<String, Object> categoryMap = getMap(category);

		ResponseEntity<Category> response = restTemplate.postForEntity(getUrl() + "/category", categoryMap, Category.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/category", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putCategoryTest() throws Exception {
		Category category = new Category("name");
		Map<String, Object> categoryMap = getMap(category);
		ResponseEntity<Category> response = restTemplate.postForEntity(getUrl() + "/category", categoryMap, Category.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI categoryEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Category> responseCategoryInserted = this.restTemplate.getForEntity(categoryEndPoint, Category.class);
		assertEquals(HttpStatus.OK, responseCategoryInserted.getStatusCode());
		assertEquals(null, responseCategoryInserted.getBody().getModDate());

		responseCategoryInserted.getBody().setName("Category modified 1");

		HttpEntity<Category> requestUpdate = new HttpEntity<Category>(responseCategoryInserted.getBody());
		ResponseEntity<Category> responseModified = this.restTemplate.exchange(categoryEndPoint, HttpMethod.PUT, requestUpdate, Category.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Category modified 1", responseModified.getBody().getName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseCategoryInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteCategoryTest() throws Exception {
		Category category = new Category("name");
		Map<String, Object> categoryMap = getMap(category);
		ResponseEntity<Category> response = restTemplate.postForEntity(getUrl() + "/category", categoryMap, Category.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI categoryEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(categoryEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}