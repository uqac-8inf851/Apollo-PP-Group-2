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

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.apollo.backend.model.Team;
import com.apollo.backend.model.User;
import com.apollo.backend.repository.TeamRepository;
import com.apollo.backend.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TeamTest extends GenericTest {

	@Autowired
	private TeamRepository teamRepository;

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
	public void saveNewTeamWithUserTest() throws Exception {
		User user = userRepository.save(new User("name", "role"));

		List<User> users = new ArrayList<User>();
		users.add(user);

		Team team = new Team("name");
		team.setUser(users);

		Team response = teamRepository.save(team);

		assertNotNull(response);
	}

	@Test
	public void saveNewTeamWithOutUserTest() throws Exception {
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
	public void postNewTeamWithUserTest() throws Exception {
		User user1 = userRepository.save(new User("name user 1", "role"));
		User user2 = userRepository.save(new User("name user 1", "role"));

		Team team = new Team("name");

		Map<String, Object> teamMap = getMap(team);
		ResponseEntity<Team> response = restTemplate.postForEntity(getUrl() + "/team", teamMap, Team.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");

		HttpEntity<String> httpEntity = new HttpEntity<>(
			getUrl() + "/user/" + user1.getId() + "\n" +
			getUrl() + "/user/" + user2.getId(), requestHeaders);

		ResponseEntity<String> relationResponse = restTemplate.exchange(response.getHeaders().getLocation() + "/users", HttpMethod.PUT, httpEntity, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		String jsonResponse = restTemplate.getForObject(response.getHeaders().getLocation() + "/users", String.class);
		JSONObject jsonObj = new JSONObject(jsonResponse).getJSONObject("_embedded");
		JSONArray jsonArray = jsonObj.getJSONArray("user");
		assertEquals("name user 1", jsonArray.getJSONObject(0).getString("name"));
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
		User user = userRepository.save(new User("name user 1", "role"));
		String userEndPoint = getUrl() + "/user/" + user.getId();

		Team team = teamRepository.save(new Team("name"));
		String teamEndPoint = getUrl() + "/team/" + team.getId();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");
		HttpEntity<String> httpEntityForPut = new HttpEntity<>(userEndPoint, requestHeaders);
		ResponseEntity<String> relationResponse = restTemplate.exchange(teamEndPoint + "/users", HttpMethod.PUT, httpEntityForPut, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		HttpHeaders requestHeadersDelete = new HttpHeaders();
		requestHeadersDelete.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> httpEntityForDelete = new HttpEntity<>(requestHeadersDelete);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(
			teamEndPoint + 
			"/users/" + 
			user.getId(), 
		HttpMethod.DELETE, httpEntityForDelete, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteMasterOfNxNRelationshipTest() throws Exception {
		User user = userRepository.save(new User("name user 1", "role"));
		String userEndPoint = getUrl() + "/user/" + user.getId();

		Team team = teamRepository.save(new Team("name"));
		String teamEndPoint = getUrl() + "/team/" + team.getId();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-type", "text/uri-list");

		HttpEntity<String> httpEntityForPut = new HttpEntity<>(userEndPoint, requestHeaders);
		ResponseEntity<String> relationResponse = restTemplate.exchange(teamEndPoint + "/users", HttpMethod.PUT, httpEntityForPut, String.class);
		assertEquals(HttpStatus.NO_CONTENT, relationResponse.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(teamEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());	
	}
}