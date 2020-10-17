package com.apollo.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.apollo.backend.repository.CategoryRepository;
import com.apollo.backend.repository.ProgramRepository;
import com.apollo.backend.repository.ProjectRepository;
import com.apollo.backend.repository.StatusRepository;
import com.apollo.backend.repository.TaskRepository;
import com.apollo.backend.repository.TeamRepository;
import com.apollo.backend.repository.TrackRepository;
import com.apollo.backend.repository.UserRepository;

public class GenericTest {

	@LocalServerPort
	private int port;

	@Autowired
	protected TestRestTemplate restTemplate;

	protected String getUrl() {
		String server = "localhost";
		String url = "http://" + server + ":" + port;

		return url;
	}

	protected Map<String, Object> getMap(Object object) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		Field[] allFields = object.getClass().getDeclaredFields();

		for (Field field : allFields) {
			field.setAccessible(true);

			try {
				Object value = field.get(object);
				map.put(field.getName(), value);
			} catch (Exception e) {
				throw e;
			}
		}

		return map;
	}

	protected ObjectMapper oMapper = new ObjectMapper();

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private StatusRepository statusRepository;

	protected void clearDatabase() {
		trackRepository.deleteAll();
		taskRepository.deleteAll();
		statusRepository.deleteAll();
		categoryRepository.deleteAll();
		projectRepository.deleteAll();
		programRepository.deleteAll();
		teamRepository.deleteAll();
		userRepository.deleteAll();
	}
}