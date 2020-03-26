package com.nfinity.ll.testaz.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailVariableEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "emailVariable", path = "emailVariable")
public interface IEmailVariableRepository extends JpaRepository<EmailVariableEntity, Long>, QuerydslPredicateExecutor<EmailVariableEntity> {

	   @Query("select e from EmailVariableEntity e where e.id = ?1")
	   EmailVariableEntity findById(long id);

	   @Query("select e from EmailVariableEntity e where e.propertyName = ?1")
	   EmailVariableEntity findByEmailName(String value);
	
}