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

import com.apollo.backend.model.Client;
import com.apollo.backend.repository.ClientRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClientTest extends GenericTest {

	@Autowired
	private ClientRepository clientRepository;

	private static boolean populatedDb = false;

	@BeforeEach
	public void setup() {
		if(populatedDb) return;

		clearDatabase();

		populatedDb = true;
	}

	@Test
	public void saveNewClientWithContactTest() throws Exception {
		Client client = new Client("companyName");

		Client response = clientRepository.save(client);

		assertNotNull(response);
	}

	@Test
	public void postNewClientTest() throws Exception {
		Client client = new Client("companyName");

		Map<String, Object> clientMap = getMap(client);

		ResponseEntity<Client> response = restTemplate.postForEntity(getUrl() + "/client", clientMap, Client.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void postGetAllClientForEntitiesTest() throws Exception {
		Client client = new Client("companyName");

		Map<String, Object> clientMap = getMap(client);

		ResponseEntity<Client> response = restTemplate.postForEntity(getUrl() + "/client", clientMap, Client.class);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		ResponseEntity<String> responseGet = restTemplate.getForEntity(getUrl() + "/client", String.class);
		
		assertEquals(HttpStatus.OK, responseGet.getStatusCode());
	}

	@Test
	public void putClientTest() throws Exception {
		Client client = new Client("companyName");
		Map<String, Object> clientMap = getMap(client);
		ResponseEntity<Client> response = restTemplate.postForEntity(getUrl() + "/client", clientMap, Client.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI clientEndPoint = response.getHeaders().getLocation();
		ResponseEntity<Client> responseClientInserted = this.restTemplate.getForEntity(clientEndPoint, Client.class);
		assertEquals(HttpStatus.OK, responseClientInserted.getStatusCode());
		assertEquals(null, responseClientInserted.getBody().getModDate());

		responseClientInserted.getBody().setCompanyName("Client modified 1");

		HttpEntity<Client> requestUpdate = new HttpEntity<Client>(responseClientInserted.getBody());
		ResponseEntity<Client> responseModified = this.restTemplate.exchange(clientEndPoint, HttpMethod.PUT, requestUpdate, Client.class);
		assertEquals(HttpStatus.OK, responseModified.getStatusCode());
		assertEquals("Client modified 1", responseModified.getBody().getCompanyName());

		int compare = response.getBody().getModDate().compareTo(responseModified.getBody().getModDate());

		assertTrue(compare < 0);
		assertEquals(responseClientInserted.getBody().getAddDate(), responseModified.getBody().getAddDate());
	}

	@Test
	public void deleteClientTest() throws Exception {
		Client client = new Client("companyName");
		Map<String, Object> clientMap = getMap(client);
		ResponseEntity<Client> response = restTemplate.postForEntity(getUrl() + "/client", clientMap, Client.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		URI clientEndPoint = response.getHeaders().getLocation();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-HTTP-Method-Override", "DELETE");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<String> responseDelete = this.restTemplate.exchange(clientEndPoint, HttpMethod.DELETE, request, String.class);

		assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
	}
}