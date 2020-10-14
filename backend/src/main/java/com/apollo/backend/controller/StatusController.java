package com.apollo.backend.controller;

import com.apollo.backend.model.Status;
import com.apollo.backend.repository.StatusRepository;

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
public class StatusController extends GenericController {

    @Autowired
    private StatusRepository repository;

    private String baseLink = Status.class.getSimpleName().toLowerCase();

    @RequestMapping(method = RequestMethod.POST, value = "/status") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Status> status) {
        return processRequest(status.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/status/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Status> status, @PathVariable Integer id) {
        Optional<Status> registered = repository.findById(id);
        registered.get().setName(status.getContent().getName());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Status status, HttpStatus httpStatus) {
        String[] messages = super.validRequest(status);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Status saved = repository.save(status);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Status.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Status.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Status> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Status.class).slash(baseLink).slash(status.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}