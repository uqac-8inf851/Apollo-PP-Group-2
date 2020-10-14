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

import com.apollo.backend.model.User;
import com.apollo.backend.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTest extends GenericTest {

	@Autowired
	private UserRepository userRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewUserTest() throws Exception {
		User user = new User("name", "role");

		User response = userRepository.save(user);

		assertNotNull(response);
	}

	@Test
	public void postNewUserTest() throws Exception {
		User user = new User("name", "role");

		Map<String, Object> userMap = getMap(user);

		ResponseEntity<User> response = restTemplate.postForEntity(getUrl() + "/user", userMap, User.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllUserForEntitiesTest() throws Exception {
		User user = new User("name", "role");

		Map<String, Object> userMap = getMap(user);

		ResponseEntity<User> response = restTemplate.postForEntity(getUrl() + "/user", userMap, User.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/user", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putUserTest() throws Exception {
		User user = new User("name", "role");
		Map<String, Object> userMap = getMap(user);
		ResponseEntity<User> response = restTemplate.postForEntity(getUrl() + "/user", userMap, User.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI userEndPoint = response.getHeaders().getLocation();
		ResponseEntity<User> responseUserInserted = this.restTemplate.getForEntity(userEndPoint, User.class);
		assertEquals(HttpStatus.OK, responseUserInserted.getStatusCode());
		assertEquals(null, responseUserInserted.getBody().getModDate());

		responseUserInserted.getBody().setName("User modified 1");

		HttpEntity<User> requestUpdate = new HttpEntity<User>(responseUserInserted.getBody());
		ResponseEntity<User> responseModified = this.restTemplate.exchange(userEndPoint, HttpMethod.PUT, requestUpdate, User.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("User modified 1", responseModified.getBody().getName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseUserInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteUserTest() throws Exception {
		User user = new User("name", "role");
		Map<String, Object> userMap = getMap(user);
		ResponseEntity<User> response = restTemplate.postForEntity(getUrl() + "/user", userMap, User.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI userEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(userEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}