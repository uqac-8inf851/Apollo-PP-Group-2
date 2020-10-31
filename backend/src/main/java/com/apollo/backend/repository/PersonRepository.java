package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "person", path = "person")
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer> {

  List<Person> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Person> S save(S person);
}