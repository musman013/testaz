package com.nfinity.ll.testaz.domain.irepository;

import com.nfinity.ll.testaz.domain.model.UserpermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "userpermission", path = "userpermission")
public interface IUserpermissionRepository extends JpaRepository<UserpermissionEntity, UserpermissionId>,QuerydslPredicateExecutor<UserpermissionEntity> {
   
}
