package com.apollo.backend.repository;

import java.util.List;

import com.apollo.backend.model.Program;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "program", path = "program")
public interface ProgramRepository extends PagingAndSortingRepository<Program, Integer> {

  List<Program> findById(@Param("id") String id);

  @Override
  @RestResource(exported = false)
  <S extends Program> S save(S program);
}