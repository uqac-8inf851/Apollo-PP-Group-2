package com.apollo.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.apollo.backend.repository.ClientRepository;

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
	private ClientRepository clientRepository;

	protected void clearDatabase() {
		clientRepository.deleteAll();
	}
}