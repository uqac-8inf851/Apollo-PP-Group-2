package com.apollo.backend.controller;

import com.apollo.backend.model.Category;
import com.apollo.backend.repository.CategoryRepository;

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
public class CategoryController extends GenericController {

    @Autowired
    private CategoryRepository repository;

    private String baseLink = Category.class.getSimpleName().toLowerCase(Locale.getDefault());

    @RequestMapping(method = RequestMethod.POST, value = "/category") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Category> category) {
        return processRequest(category.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/category/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Category> category, @PathVariable Integer id) {
        Optional<Category> registered = repository.findById(id);
        registered.get().setName(category.getContent().getName());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Category category, HttpStatus httpCategory) {
        String[] messages = super.validRequest(category);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Category saved = repository.save(category);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Category.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Category.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Category> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Category.class).slash(baseLink).slash(category.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpCategory);
    }
}