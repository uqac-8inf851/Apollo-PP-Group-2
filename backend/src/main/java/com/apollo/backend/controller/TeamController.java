package com.apollo.backend.controller;

import com.apollo.backend.model.Team;
import com.apollo.backend.repository.TeamRepository;

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
public class TeamController extends GenericController {

    @Autowired
    private TeamRepository repository;

    private String baseLink = Team.class.getSimpleName().toLowerCase();

    @RequestMapping(method = RequestMethod.POST, value = "/team") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Team> team) {
        return processRequest(team.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/team/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Team> team, @PathVariable Integer id) {
        Optional<Team> registered = repository.findById(id);
        registered.get().setName(team.getContent().getName());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Team team, HttpStatus httpStatus) {
        String[] messages = super.validRequest(team);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Team saved = repository.save(team);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Team.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Team.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Team> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Team.class).slash(baseLink).slash(team.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}