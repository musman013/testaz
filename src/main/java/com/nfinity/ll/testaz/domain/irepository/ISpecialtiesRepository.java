package com.nfinity.ll.testaz.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity; 
import java.util.List;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "specialties", path = "specialties")
public interface ISpecialtiesRepository extends JpaRepository<SpecialtiesEntity, Integer>,QuerydslPredicateExecutor<SpecialtiesEntity> {

}
