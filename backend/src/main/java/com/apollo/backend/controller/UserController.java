package com.apollo.backend.controller;

import com.apollo.backend.model.User;
import com.apollo.backend.repository.UserRepository;

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
public class UserController extends GenericController {

    @Autowired
    private UserRepository repository;

    private String baseLink = User.class.getSimpleName().toLowerCase();

    @RequestMapping(method = RequestMethod.POST, value = "/user") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<User> user) {
        return processRequest(user.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<User> user, @PathVariable Integer id) {
        Optional<User> registered = repository.findById(id);
        registered.get().setName(user.getContent().getName());
        registered.get().setRole(user.getContent().getRole());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(User user, HttpStatus httpStatus) {
        String[] messages = super.validRequest(user);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        User saved = repository.save(user);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(User.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(User.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<User> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(User.class).slash(baseLink).slash(user.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}