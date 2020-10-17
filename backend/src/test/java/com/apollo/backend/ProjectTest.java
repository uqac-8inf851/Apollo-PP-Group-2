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
import com.apollo.backend.repository.ProjectRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectTest extends GenericTest {

	@Autowired
	private ProjectRepository projectRepository;

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
	public void saveNewProjectTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);

		Project response = projectRepository.save(project);

		assertNotNull(response);
	}

	@Test
	public void postNewProjectTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);

		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());

		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllProjectForEntitiesTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);

		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());

		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/project", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putProjectTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI projectEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Project> responseProjectInserted = this.restTemplate.getForEntity(projectEndPoint, Project.class);
		assertEquals(HttpStatus.OK, responseProjectInserted.getStatusCode());
		assertEquals(null, responseProjectInserted.getBody().getModDate());

		responseProjectInserted.getBody().setTitle("Project modified 1");

		HttpEntity<Project> requestUpdate = new HttpEntity<Project>(responseProjectInserted.getBody());
		ResponseEntity<Project> responseModified = this.restTemplate.exchange(projectEndPoint, HttpMethod.PUT, requestUpdate, Project.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Project modified 1", responseModified.getBody().getTitle());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseProjectInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteProjectTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI projectEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(projectEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}