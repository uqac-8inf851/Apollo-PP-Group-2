package com.apollo.backend.service;

import java.util.List;
import java.util.Optional;

import com.apollo.backend.domain.TeamDomain;
import com.apollo.backend.model.Team;
import com.apollo.backend.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Primary
@Repository("teamService")
public class TeamService implements TeamRepository {

    @Autowired
    @Qualifier("teamRepository")
    private TeamRepository repository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Iterable<Team> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Team> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<Team> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<Team> findAllById(Iterable<Integer> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<Team> findById(Integer idTeam) {
        return repository.findById(idTeam);
    }

    @Override
    public List<Team> findById(String idTeam) {
        return repository.findById(idTeam);
    }

    @Override
    public boolean existsById(Integer idTeam) {
        return repository.existsById(idTeam);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Integer idTeam) {
        repository.deleteById(idTeam);
    }

    @Override
    public void delete(Team entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<? extends Team> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public <S extends Team> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public <S extends Team> S save(S team) {

        TeamDomain teamDomain = applicationContext.getBean(TeamDomain.class);
        
        return repository.save(team);
    }
}
