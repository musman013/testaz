package com.nfinity.ll.testaz.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "permission", path = "permission")
public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long>, QuerydslPredicateExecutor<PermissionEntity> {

    @Query("select u from PermissionEntity u where u.name = ?1")
    PermissionEntity findByPermissionName(String value);
}
