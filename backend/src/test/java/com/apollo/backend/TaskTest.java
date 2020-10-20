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
import java.time.Instant;
import java.util.Map;

import com.apollo.backend.model.Category;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.model.Status;
import com.apollo.backend.model.Task;
import com.apollo.backend.model.Track;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;
import com.apollo.backend.repository.TaskRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskTest extends GenericTest {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StatusRepository statusRepository;

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

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);

		Task response = taskRepository.save(task);

		assertNotNull(response);
	}

	@Test
	public void postNewTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));
		
		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);

		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());

		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllTaskForEntitiesTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);

		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());

		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/task", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putTaskTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI taskEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Task> responseTaskInserted = this.restTemplate.getForEntity(taskEndPoint, Task.class);
		assertEquals(HttpStatus.OK, responseTaskInserted.getStatusCode());
		assertEquals(null, responseTaskInserted.getBody().getModDate());

		responseTaskInserted.getBody().setTitle("Task modified 1");

		Map<String, Object> taskMapInserted = getMap(responseTaskInserted.getBody());
		taskMapInserted.put("category", getUrl() + "/category/" + category.getId());
		taskMapInserted.put("status", getUrl() + "/status/" + status.getId());

		HttpEntity<Map<String, Object>> requestUpdate = new HttpEntity<Map<String, Object>>(taskMapInserted);
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

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", getUrl() + "/project/" + project.getId());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> response = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI taskEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(taskEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteTasktWithTrackTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> responseProject = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, responseProject.getStatusCode());

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", responseProject.getHeaders().getLocation());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> responseTask = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, responseTask.getStatusCode());

		Track track = new Track(Instant.now(), Instant.now(), task);
		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", responseTask.getHeaders().getLocation());
		ResponseEntity<Track> responseTrack = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		assertEquals(HttpStatus.CREATED, responseTrack.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(responseTask.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.CONFLICT, responseDelete.getStatusCode());
	}

	@Test
	public void deleteTasktWithTrackDeletedTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = new Project("title", "description", program);
		Map<String, Object> projectMap = getMap(project);
		projectMap.put("program", getUrl() + "/program/" + program.getId());
		ResponseEntity<Project> responseProject = restTemplate.postForEntity(getUrl() + "/project", projectMap, Project.class);
		assertEquals(HttpStatus.CREATED, responseProject.getStatusCode());

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = new Task("title", "description", 0, project, category, status);
		Map<String, Object> taskMap = getMap(task);
		taskMap.put("project", responseProject.getHeaders().getLocation());
		taskMap.put("category", getUrl() + "/category/" + category.getId());
		taskMap.put("status", getUrl() + "/status/" + status.getId());
		ResponseEntity<Task> responseTask = restTemplate.postForEntity(getUrl() + "/task", taskMap, Task.class);
		assertEquals(HttpStatus.CREATED, responseTask.getStatusCode());

		Track track = new Track(Instant.now(), Instant.now(), task);
		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", responseTask.getHeaders().getLocation());
		ResponseEntity<Track> responseTrack = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		assertEquals(HttpStatus.CREATED, responseTrack.getStatusCode());

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> responseDeleteTrack = this.restTemplate.exchange(responseTrack.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDeleteTrack.getStatusCode());

		ResponseEntity<String> responseDelete = this.restTemplate.exchange(responseTask.getHeaders().getLocation(), HttpMethod.DELETE, request, String.class);
		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}