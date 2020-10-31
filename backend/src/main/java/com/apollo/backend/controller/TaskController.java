package com.apollo.backend.controller;

import com.apollo.backend.model.Task;
import com.apollo.backend.repository.TaskRepository;

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
public class TaskController extends GenericController {

    @Autowired
    private TaskRepository repository;

    private String baseLink = Task.class.getSimpleName().toLowerCase(Locale.getDefault());

    @RequestMapping(method = RequestMethod.POST, value = "/task") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Task> task) {
        return processRequest(task.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/task/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Task> task, @PathVariable Integer id) {
        Optional<Task> registered = repository.findById(id);
        registered.get().setTaskTitle(task.getContent().getTaskTitle());
        registered.get().setTaskDescription(task.getContent().getTaskDescription());
        registered.get().setPriority(task.getContent().getPriority());
        registered.get().setCategory(task.getContent().getCategory());
        registered.get().setStatus(task.getContent().getStatus());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Task task, HttpStatus httpStatus) {
        String[] messages = super.validRequest(task);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Task saved = repository.save(task);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Task.class).slash(baseLink).slash(saved.getId()).slash("tracks").withRel("tracks"));
        links.add(linkTo(Task.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Task.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Task> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Task.class).slash(baseLink).slash(task.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}