package com.apollo.backend;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest extends GenericTest {

	@Test
	public void testHttpStatusOkForEntity() throws Exception {
		ResponseEntity<String> response = this.restTemplate.getForEntity(getUrl(), String.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}