package com.apollo.backend.controller;

import com.apollo.backend.model.Track;
import com.apollo.backend.repository.TrackRepository;

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
public class TrackController extends GenericController {

    @Autowired
    private TrackRepository repository;

    private String baseLink = Track.class.getSimpleName().toLowerCase();

    @RequestMapping(method = RequestMethod.POST, value = "/track") 
    public @ResponseBody ResponseEntity<?> savePost(@RequestBody @Valid EntityModel<Track> track) {
        return processRequest(track.getContent(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/track/{id}") 
    public @ResponseBody ResponseEntity<?> savePut(@RequestBody @Valid EntityModel<Track> track, @PathVariable Integer id) {
        Optional<Track> registered = repository.findById(id);
        registered.get().setStartTime(track.getContent().getStartTime());
        registered.get().setEndTime(track.getContent().getEndTime());
        registered.get().setUser(track.getContent().getUser());

        return processRequest(registered.get(), HttpStatus.OK);
    }

    private ResponseEntity<?> processRequest(Track track, HttpStatus httpStatus) {
        String[] messages = super.validRequest(track);

        if(messages.length > 0) {
            EntityModel<String[]> resource = new EntityModel<String[]>(messages);

            return new ResponseEntity<>(resource , HttpStatus.BAD_REQUEST);
        }

        Track saved = repository.save(track);

        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(Track.class).slash(baseLink).slash(saved.getId()).withRel(baseLink));
        links.add(linkTo(Track.class).slash(baseLink).slash(saved.getId()).withSelfRel());

        EntityModel<Track> resource = new EntityModel<>(saved);
        resource.add(links);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", linkTo(Track.class).slash(baseLink).slash(track.getId()).toString());

        return new ResponseEntity<>(resource, headers, httpStatus);
    }
}