package com.apollo.backend.controller;

import com.apollo.backend.model.Project;
import com.apollo.backend.repository.ProjectRepository;

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
public class ProjectController extends GenericController {

    @Autowired
    private ProjectRepository repository;

    private String baseLink = Project.class.getSimpleName().toLowerCase(Locale.getDefault());

    @RequestMapping(method = RequestMethod.POST, value = "/project") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Project> project) {
        return processRequest(project.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/project/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Project> project, @PathVariable Integer id) {
        Optional<Project> registered = repository.findById(id);
        registered.get().setTitle(project.getContent().getTitle());
        registered.get().setDescription(project.getContent().getDescription());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Project project, HttpStatus httpStatus) {
        String[] messages = super.validRequest(project);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Project saved = repository.save(project);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Project.class).slash(baseLink).slash(saved.getId()).slash("tasks").withRel("tasks"));
        links.add(linkTo(Project.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Project.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Project> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Project.class).slash(baseLink).slash(project.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}