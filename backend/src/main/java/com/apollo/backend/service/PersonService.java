package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.PersonDomain;
import com.apollo.backend.model.Person;
import com.apollo.backend.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("personService")
public class PersonService implements PersonRepository {

    @Autowired
    @Qualifier("personRepository")
    private PersonRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Person> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Person> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Person> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Person> findById(Integer idPerson) {
        return repository.findById(idPerson);
    }

    @Override
    public List<Person> findById(String idPerson) {
        return repository.findById(idPerson);
    }

    @Override
    public boolean existsById(Integer idPerson) {
        return repository.existsById(idPerson);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Integer idPerson) {
        repository.deleteById(idPerson);
    }

    @Override
    public void delete(Person entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Person> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Person> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Person> S save(S person) {

        PersonDomain personDomain = applicationContext.getBean(PersonDomain.class);
        
        return repository.save(person);
    }
}
