package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.ProgramDomain;
import com.apollo.backend.model.Program;
import com.apollo.backend.repository.ProgramRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("programService")
public class ProgramService implements ProgramRepository {

    @Autowired
    @Qualifier("programRepository")
    private ProgramRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Program> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Program> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Program> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Program> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Program> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Program> findById(String id) {
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
    public void delete(Program entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Program> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Program> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Program> S save(S program) {

        ProgramDomain programDomain = applicationContext.getBean(ProgramDomain.class);
        
        return repository.save(program);
    }
}
