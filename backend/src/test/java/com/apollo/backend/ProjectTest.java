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

import com.apollo.backend.model.Category;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.model.Status;
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectTest extends GenericTest {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired 
	private ProgramRepository programRepository;

	@Autowired
	private CategoryRepository categoryRepository;

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

		responseProjectInserted.getBody().setProjectTitle("Project modified 1");

		HttpEntity<Project> requestUpdate = new HttpEntity<Project>(responseProjectInserted.getBody());
		ResponseEntity<Project> responseModified = this.restTemplate.exchange(projectEndPoint, HttpMethod.PUT, requestUpdate, Project.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Project modified 1", responseModified.getBody().getProjectTitle());

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

	@Test
	public void deleteProjectWithTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", response.getHeaders().getLocation());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> responseTask = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, responseTask.getStatusCode());

		URI projectEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(projectEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.CONFLICT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteProjectWithTaskDeletedTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> response = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", response.getHeaders().getLocation());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> responseTask = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, responseTask.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> responseDeleteTask = this.restTemplate.exchange(responseTask.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDeleteTask.getStatusCode());

		ResponseEntity<String> responseDelete = this.restTemplate.exchange(response.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}