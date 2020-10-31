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

import com.apollo.backend.model.Person;
import com.apollo.backend.repository.PersonRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonTest extends GenericTest {

	@Autowired
	private PersonRepository personRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewPersonTest() throws Exception {
		Person person = new Person("name", "role");

		Person response = personRepository.save(person);

		assertNotNull(response);
	}

	@Test
	public void postNewPersonTest() throws Exception {
		Person person = new Person("name", "role");

		Map<String, Object> personMap = getMap(person);

		ResponseEntity<Person> response = restTemplate.postForEntity(getUrl() + "/person", personMap, Person.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllPersonForEntitiesTest() throws Exception {
		Person person = new Person("name", "role");

		Map<String, Object> personMap = getMap(person);

		ResponseEntity<Person> response = restTemplate.postForEntity(getUrl() + "/person", personMap, Person.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/person", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putPersonTest() throws Exception {
		Person person = new Person("name", "role");
		Map<String, Object> personMap = getMap(person);
		ResponseEntity<Person> response = restTemplate.postForEntity(getUrl() + "/person", personMap, Person.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI personEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Person> responsePersonInserted = this.restTemplate.getForEntity(personEndPoint, Person.class);
		assertEquals(HttpStatus.OK, responsePersonInserted.getStatusCode());
		assertEquals(null, responsePersonInserted.getBody().getModDate());

		responsePersonInserted.getBody().setName("Person modified 1");

		HttpEntity<Person> requestUpdate = new HttpEntity<Person>(responsePersonInserted.getBody());
		ResponseEntity<Person> responseModified = this.restTemplate.exchange(personEndPoint, HttpMethod.PUT, requestUpdate, Person.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Person modified 1", responseModified.getBody().getName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responsePersonInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deletePersonTest() throws Exception {
		Person person = new Person("name", "role");
		Map<String, Object> personMap = getMap(person);
		ResponseEntity<Person> response = restTemplate.postForEntity(getUrl() + "/person", personMap, Person.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI personEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(personEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}