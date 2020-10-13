package com.apollo.backend.controller;

import com.apollo.backend.model.Client;
import com.apollo.backend.repository.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@CrossOrigin
@RepositoryRestController
public class ClientController extends GenericController {

    @Autowired
    private ClientRepository repository;

    private String baseLink = Client.class.getSimpleName().toLowerCase();

    @RequestMapping(method = RequestMethod.POST, value = "/client") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Client> client) {
        return processRequest(client.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/client/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Client> client, @PathVariable Integer id) {
        Optional<Client> registered = repository.findById(id);
        registered.get().setCompanyName(client.getContent().getCompanyName());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Client client, HttpStatus httpStatus) {
        String[] messages = super.validRequest(client);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Client saved = repository.save(client);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Client.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Client.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Client> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Client.class).slash(baseLink).slash(client.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}