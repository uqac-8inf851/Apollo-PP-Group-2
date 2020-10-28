package com.apollo.backend.controller;

import com.apollo.backend.model.Program;
import com.apollo.backend.repository.ProgramRepository;

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
public class ProgramController extends GenericController {

    @Autowired
    private ProgramRepository repository;

    private String baseLink = Program.class.getSimpleName().toLowerCase(Locale.getDefault());

    @RequestMapping(method = RequestMethod.POST, value = "/program") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Program> program) {
        return processRequest(program.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/program/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Program> program, @PathVariable Integer id) {
        Optional<Program> registered = repository.findById(id);
        registered.get().setTitle(program.getContent().getTitle());
        registered.get().setDescription(program.getContent().getDescription());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Program program, HttpStatus httpStatus) {
        String[] messages = super.validRequest(program);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Program saved = repository.save(program);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Program.class).slash(baseLink).slash(saved.getId()).slash("projects").withRel("projects"));
        links.add(linkTo(Program.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Program.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Program> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Program.class).slash(baseLink).slash(program.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}