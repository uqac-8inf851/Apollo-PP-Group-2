package com.apollo.backend.controller;

import com.apollo.backend.model.Person;
import com.apollo.backend.repository.PersonRepository;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

@CrossOrigin
@RepositoryRestController
public class PersonController extends GenericController {

    @Autowired
    private PersonRepository repository;

    private String baseLink = Person.class.getSimpleName().toLowerCase(Locale.getDefault());

    @RequestMapping(method = RequestMethod.POST, value = "/person") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Person> person) {
        return processRequest(person.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/person/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Person> person, @PathVariable Integer id) {
        Optional<Person> registered = repository.findById(id);
        registered.get().setName(person.getContent().getName());
        registered.get().setRole(person.getContent().getRole());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Person person, HttpStatus httpStatus) {
        String[] messages = super.validRequest(person);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Person saved = repository.save(person);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Person.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Person.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Person> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Person.class).slash(baseLink).slash(person.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}