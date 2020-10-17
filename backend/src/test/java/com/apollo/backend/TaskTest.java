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
import com.apollo.backend.model.Task;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.TaskRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskTest extends GenericTest {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProjectRepository projectRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Task task = new Task("title", "description", 0, project);

		Task response = taskRepository.save(task);

		assertNotNull(response);
	}

	@Test
	public void postNewTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		
		Task task = new Task("title", "description", 0, project);

		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());

		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllTaskForEntitiesTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Task task = new Task("title", "description", 0, project);

		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());

		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/task", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Task task = new Task("title", "description", 0, project);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI taskEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Task> responseTaskInserted = this.restTemplate.getForEntity(taskEndPoint, Task.class);
		assertEquals(HttpStatus.OK, responseTaskInserted.getStatusCode());
		assertEquals(null, responseTaskInserted.getBody().getModDate());

		responseTaskInserted.getBody().setTitle("Task modified 1");

		HttpEntity<Task> requestUpdate = new HttpEntity<Task>(responseTaskInserted.getBody());
		ResponseEntity<Task> responseModified = this.restTemplate.exchange(taskEndPoint, HttpMethod.PUT, requestUpdate, Task.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Task modified 1", responseModified.getBody().getTitle());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseTaskInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Task task = new Task("title", "description", 0, project);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI taskEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(taskEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}