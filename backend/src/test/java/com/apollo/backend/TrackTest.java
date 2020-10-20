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
import com.apollo.backend.model.Task;
import com.apollo.backend.model.Track;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.TaskRepository;
import com.apollo.backend.repository.TrackRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TrackTest extends GenericTest {

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

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewTrackTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category));

		Track track = new Track(Instant.now(), Instant.now(), task);

		Track response = trackRepository.save(track);

		assertNotNull(response);
	}

	@Test
	public void postNewTrackTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category));

		Track track = new Track(Instant.now(), Instant.now(), task);

		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", getUrl() + "/task/" + task.getId());

		ResponseEntity<Track> response = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllTrackForEntitiesTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category));

		Track track = new Track(Instant.now(), Instant.now(), task);

		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", getUrl() + "/task/" + task.getId());

		ResponseEntity<Track> response = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/track", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putTrackTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category));

		Track track = new Track(Instant.now(), Instant.now(), task);
		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", getUrl() + "/task/" + task.getId());
		ResponseEntity<Track> response = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI trackEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Track> responseTrackInserted = this.restTemplate.getForEntity(trackEndPoint, Track.class);
		assertEquals(HttpStatus.OK, responseTrackInserted.getStatusCode());
		assertEquals(null, responseTrackInserted.getBody().getModDate());

		Instant now = Instant.now().plusSeconds(100);

		responseTrackInserted.getBody().setEndTime(now);

		HttpEntity<Track> requestUpdate = new HttpEntity<Track>(responseTrackInserted.getBody());
		ResponseEntity<Track> responseModified = this.restTemplate.exchange(trackEndPoint, HttpMethod.PUT, requestUpdate, Track.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals(now, responseModified.getBody().getEndTime());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseTrackInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteTrackTest() throws Exception {
		Program program = programRepository.save(new Program("title", "description"));

		Project project = projectRepository.save(new Project("title", "description", program));

		Category category = categoryRepository.save(new Category("name"));

		Task task = taskRepository.save(new Task("title", "description", 0, project, category));

		Track track = new Track(Instant.now(), Instant.now(), task);
		Map<String, Object> trackMap = getMap(track);
		trackMap.put("task", getUrl() + "/task/" + task.getId());
		ResponseEntity<Track> response = restTemplate.postForEntity(getUrl() + "/track", trackMap, Track.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI trackEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(trackEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}