package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamDomain {

    @Autowired
    private TeamRepository repository;

    @Autowired
    private EntityManager entityManager;
}