package com.nfinity.ll.testaz.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.ll.testaz.domain.model.RoleEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface IRoleRepository extends JpaRepository<RoleEntity, Long>, QuerydslPredicateExecutor<RoleEntity> {

 //   @Query("select u from RoleEntity u where u.id = ?1")
 //   RoleEntity findById(long id);

    @Query("select u from RoleEntity u where u.name = ?1")
    RoleEntity findByRoleName(String value);
}