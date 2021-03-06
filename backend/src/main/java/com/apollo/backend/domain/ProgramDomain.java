package com.apollo.backend.domain;

import javax.persistence.EntityManager;

import com.apollo.backend.repository.ProgramRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramDomain {

    @Autowired
    private ProgramRepository repository;

    @Autowired
    private EntityManager entityManager;
}