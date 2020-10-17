package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.TrackDomain;
import com.apollo.backend.model.Track;
import com.apollo.backend.repository.TrackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("trackService")
public class TrackService implements TrackRepository {

    @Autowired
    @Qualifier("trackRepository")
    private TrackRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Track> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Track> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Track> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Track> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Track> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Track> findById(String id) {
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
    public void delete(Track entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Track> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Track> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Track> S save(S track) {

        TrackDomain trackDomain = applicationContext.getBean(TrackDomain.class);
        
        return repository.save(track);
    }
}
