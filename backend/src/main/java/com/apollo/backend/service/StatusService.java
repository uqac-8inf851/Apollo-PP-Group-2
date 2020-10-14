package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.StatusDomain;
import com.apollo.backend.model.Status;
import com.apollo.backend.repository.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("statusService")
public class StatusService implements StatusRepository {

    @Autowired
    @Qualifier("statusRepository")
    private StatusRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Status> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Status> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Status> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Status> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Status> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Status> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Status entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Status> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Status> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Status> S save(S status) {

        StatusDomain statusDomain = applicationContext.getBean(StatusDomain.class);
        
        return repository.save(status);
    }
}
