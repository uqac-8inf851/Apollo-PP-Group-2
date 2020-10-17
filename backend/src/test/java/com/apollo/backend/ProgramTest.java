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

import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.repository.ProgramRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProgramTest extends GenericTest {

	@Autowired
	private ProgramRepository programRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewProgramTest() throws Exception {
		Program program = new Program("title", "description");

		Program response = programRepository.save(program);

		assertNotNull(response);
	}

	@Test
	public void postNewProgramTest() throws Exception {
		Program program = new Program("title", "description");

		Map<String, Object> programMap = getMap(program);

		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllProgramForEntitiesTest() throws Exception {
		Program program = new Program("title", "description");

		Map<String, Object> programMap = getMap(program);

		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/program", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putProgramTest() throws Exception {
		Program program = new Program("title", "description");
		Map<String, Object> programMap = getMap(program);
		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI programEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Program> responseProgramInserted = this.restTemplate.getForEntity(programEndPoint, Program.class);
		assertEquals(HttpStatus.OK, responseProgramInserted.getStatusCode());
		assertEquals(null, responseProgramInserted.getBody().getModDate());

		responseProgramInserted.getBody().setTitle("Program modified 1");

		HttpEntity<Program> requestUpdate = new HttpEntity<Program>(responseProgramInserted.getBody());
		ResponseEntity<Program> responseModified = this.restTemplate.exchange(programEndPoint, HttpMethod.PUT, requestUpdate, Program.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Program modified 1", responseModified.getBody().getTitle());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseProgramInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteProgramTest() throws Exception {
		Program program = new Program("title", "description");
		Map<String, Object> programMap = getMap(program);
		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI programEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(programEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteProgramWithProjectTest() throws Exception {
		Program program = new Program("title", "description");
		Map<String, Object> programMap = getMap(program);
		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", response.getHeaders().getLocation());
		ResponseEntity<Project> responseProject = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, responseProject.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(response.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.CONFLICT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteProgramWithProjectDeletedTest() throws Exception {
		Program program = new Program("title", "description");
		Map<String, Object> programMap = getMap(program);
		ResponseEntity<Program> response = restTemplate.postForEntity(getUrl() + "/program", programMap, Program.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", response.getHeaders().getLocation());
		ResponseEntity<Project> responseProject = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, responseProject.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> responseDeleteProject = this.restTemplate.exchange(responseProject.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDeleteProject.getStatusCode());

		ResponseEntity<String> responseDelete = this.restTemplate.exchange(response.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}