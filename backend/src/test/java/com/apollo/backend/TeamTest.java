package com.apollo.backend;

import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.apollo.backend.model.Team;
import com.apollo.backend.model.Person;
import com.apollo.backend.repository.TeamRepository;
import com.apollo.backend.repository.PersonRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TeamTest extends GenericTest {

	@Autowired
	private TeamRepository teamRepository;

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
	public void saveNewTeamWithPersonTest() throws Exception {
		Person person = personRepository.save(new Person("name", "role"));

		List<Person> persons = new ArrayList<Person>();
		persons.add(person);

		Team team = new Team("name");
		team.setPerson(persons);

		Team response = teamRepository.save(team);

		assertNotNull(response);
	}

	@Test
	public void saveNewTeamWithOutPersonTest() throws Exception {
		Team team = new Team("name");

		Team response = teamRepository.save(team);

		assertNotNull(response);
	}

	@Test
	public void postNewTeamTest() throws Exception {
		Team team = new Team("name");

		Map<String, Object> teamMap = getMap(team);

		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postNewTeamWithPersonTest() throws Exception {
		Person person1 = personRepository.save(new Person("name person 1", "role"));
		Person person2 = personRepository.save(new Person("name person 1", "role"));

		Team team = new Team("name");

		Map<String, Object> teamMap = getMap(team);
		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");

		HttpEntity<String> httpEntity = new HttpEntity<>(
			getUrl() + "/person/" + person1.getId() + "\n" +
			getUrl() + "/person/" + person2.getId(), requestHeaders);

		ResponseEntity<String> relationResponse = restTemplate.exchange(response.getHeaders().getLocation() + "/persons", HttpMethod.PUT, httpEntity, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		String jsonResponse = restTemplate.getForObject(response.getHeaders().getLocation() + "/persons", String.class);
		JSONObject jsonObj = new JSONObject(jsonResponse).getJSONObject("_embedded");
		JSONArray jsonArray = jsonObj.getJSONArray("person");
		assertEquals("name person 1", jsonArray.getJSONObject(0).getString("name"));
	}

	@Test
	public void postGetAllTeamForEntitiesTest() throws Exception {
		Team team = new Team("name");

		Map<String, Object> teamMap = getMap(team);

		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/team", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putTeamTest() throws Exception {
		Team team = new Team("name");
		Map<String, Object> teamMap = getMap(team);
		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI teamEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Team> responseTeamInserted = this.restTemplate.getForEntity(teamEndPoint, Team.class);
		assertEquals(HttpStatus.OK, responseTeamInserted.getStatusCode());
		assertEquals(null, responseTeamInserted.getBody().getModDate());

		responseTeamInserted.getBody().setName("Team modified 1");

		HttpEntity<Team> requestUpdate = new HttpEntity<Team>(responseTeamInserted.getBody());
		ResponseEntity<Team> responseModified = this.restTemplate.exchange(teamEndPoint, HttpMethod.PUT, requestUpdate, Team.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Team modified 1", responseModified.getBody().getName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseTeamInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteTeamTest() throws Exception {
		Team team = new Team("name");
		Map<String, Object> teamMap = getMap(team);
		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI teamEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(teamEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteNxNRelationshipTest() throws Exception {
		Person person = personRepository.save(new Person("name person 1", "role"));
		String personEndPoint = getUrl() + "/person/" + person.getId();

		Team team = teamRepository.save(new Team("name"));
		String teamEndPoint = getUrl() + "/team/" + team.getId();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");
		HttpEntity<String> httpEntityForPut = new HttpEntity<>(personEndPoint, requestHeaders);
		ResponseEntity<String> relationResponse = restTemplate.exchange(teamEndPoint + "/persons", HttpMethod.PUT, httpEntityForPut, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		HttpHeaders requestHeadersDelete = new HttpHeaders();
		requestHeadersDelete.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> httpEntityForDelete = new HttpEntity<>(requestHeadersDelete);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(
			teamEndPoint + 
			"/persons/" + 
			person.getId(), 
		HttpMethod.DELETE, httpEntityForDelete, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteMasterOfNxNRelationshipTest() throws Exception {
		Person person = personRepository.save(new Person("name person 1", "role"));
		String personEndPoint = getUrl() + "/person/" + person.getId();

		Team team = teamRepository.save(new Team("name"));
		String teamEndPoint = getUrl() + "/team/" + team.getId();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");

		HttpEntity<String> httpEntityForPut = new HttpEntity<>(personEndPoint, requestHeaders);
		ResponseEntity<String> relationResponse = restTemplate.exchange(teamEndPoint + "/persons", HttpMethod.PUT, httpEntityForPut, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(teamEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());	
	}
}