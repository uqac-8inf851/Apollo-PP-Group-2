package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonDomain {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private EntityManager entityManager;
}