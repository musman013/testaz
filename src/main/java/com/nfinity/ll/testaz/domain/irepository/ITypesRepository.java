package com.nfinity.ll.testaz.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.ll.testaz.domain.model.PetsEntity; 
import java.util.List;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "types", path = "types")
public interface ITypesRepository extends JpaRepository<TypesEntity, Integer>,QuerydslPredicateExecutor<TypesEntity> {

}
