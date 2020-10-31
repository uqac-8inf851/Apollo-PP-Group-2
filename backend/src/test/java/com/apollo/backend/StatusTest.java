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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Map;

import com.apollo.backend.model.Status;
import com.apollo.backend.repository.StatusRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StatusTest extends GenericTest {

	@Autowired
	private StatusRepository statusRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewStatusTest() throws Exception {
		Status status = new Status("name");

		Status response = statusRepository.save(status);

		assertNotNull(response);
	}

	@Test
	public void postNewStatusTest() throws Exception {
		Status status = new Status("name");

		Map<String, Object> statusMap = getMap(status);

		ResponseEntity<Status> response = restTemplate.postForEntity(getUrl() + "/status", statusMap, Status.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllStatusForEntitiesTest() throws Exception {
		Status status = new Status("name");

		Map<String, Object> statusMap = getMap(status);

		ResponseEntity<Status> response = restTemplate.postForEntity(getUrl() + "/status", statusMap, Status.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/status", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putStatusTest() throws Exception {
		Status status = new Status("name");
		Map<String, Object> statusMap = getMap(status);
		ResponseEntity<Status> response = restTemplate.postForEntity(getUrl() + "/status", statusMap, Status.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI statusEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Status> responseStatusInserted = this.restTemplate.getForEntity(statusEndPoint, Status.class);
		assertEquals(HttpStatus.OK, responseStatusInserted.getStatusCode());
		assertEquals(null, responseStatusInserted.getBody().getModDate());

		responseStatusInserted.getBody().setName("Status modified 1");

		HttpEntity<Status> requestUpdate = new HttpEntity<Status>(responseStatusInserted.getBody());
		ResponseEntity<Status> responseModified = this.restTemplate.exchange(statusEndPoint, HttpMethod.PUT, requestUpdate, Status.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Status modified 1", responseModified.getBody().getName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseStatusInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteStatusTest() throws Exception {
		Status status = new Status("name");
		Map<String, Object> statusMap = getMap(status);
		ResponseEntity<Status> response = restTemplate.postForEntity(getUrl() + "/status", statusMap, Status.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI statusEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(statusEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}