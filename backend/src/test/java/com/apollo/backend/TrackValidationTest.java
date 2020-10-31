package com.apollo.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.model.Category;
import com.apollo.backend.model.Program;
import com.apollo.backend.model.Project;
import com.apollo.backend.model.Status;
import com.apollo.backend.model.Task;
import com.apollo.backend.model.Track;
import com.apollo.backend.model.User;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;
import com.apollo.backend.repository.TaskRepository;
import com.apollo.backend.repository.TrackRepository;
import com.apollo.backend.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TrackValidationTest extends GenericTest {

	@Autowired
	private TrackRepository trackRepository;

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

	@Autowired
	private UserRepository userRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) { return; }

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void testSave() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track newObject = trackRepository.save(new Track(Instant.now(), Instant.now(), task, user));

		assertNotNull(newObject);
	}

	@Test
	public void testPostJsonEmpty() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPostJsonNewProperty() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", Instant.now());
		map.put("endTime", Instant.now());
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostReturnPatternCreated() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track track = new Track(Instant.now(), Instant.now(), task, user);
		Map<String, Object> map = getMap(track);
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void testPostNameNotNull() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track track = new Track(null, null, task, user);
		Map<String, Object> map = getMap(track);
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track", HttpMethod.POST, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Start Time is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}


	@Test
	public void testPutJsonEmpty() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));
		
		Track saved = trackRepository.save(new Track(Instant.now(), Instant.now(), task, user));

		Map<String, Object> map = new HashMap<String, Object>();

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutJsonNewProperty() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track saved = trackRepository.save(new Track(Instant.now(), Instant.now(), task, user));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", Instant.now());
		map.put("endTime", Instant.now());
		map.put("anotherProperty", "intruder");
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutReturnPatternOk() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track saved = trackRepository.save(new Track(Instant.now(), Instant.now(), task, user));
		
		Map<String, Object> map = getMap(saved);
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track/" + saved.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testPutStartTimeNotNull() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Status status = statusRepository.save(new Status("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category, status));

		User user = userRepository.save(new User("name", "role"));

		Track track = trackRepository.save(new Track(Instant.now(), Instant.now(), task, user));
		track.setStartTime(null);
		Map<String, Object> map = getMap(track);
		map.put("task", getUrl() + "/task/" + task.getId());
		map.put("user", getUrl() + "/user/" + user.getId());

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);

		ResponseEntity<String> response = restTemplate.exchange(getUrl() + "/track/" + track.getId(), HttpMethod.PUT, httpEntity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readTree("{\"content\" : [ \"Start Time is mandatory\" ]}"), mapper.readTree(response.getBody()));
	}
}