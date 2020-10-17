package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.TrackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackDomain {

    @Autowired
    private TrackRepository repository;

    @Autowired
    private EntityManager entityManager;
}