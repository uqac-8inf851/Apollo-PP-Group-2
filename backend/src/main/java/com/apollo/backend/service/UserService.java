package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.UserDomain;
import com.apollo.backend.model.User;
import com.apollo.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("userService")
public class UserService implements UserRepository {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<User> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<User> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findById(String id) {
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
    public void delete(User entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends User> S save(S user) {

        UserDomain userDomain = applicationContext.getBean(UserDomain.class);
        
        return repository.save(user);
    }
}
