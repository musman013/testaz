package com.nfinity.ll.testaz.domain.irepository;

import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "rolepermission", path = "rolepermission")
public interface IRolepermissionRepository extends JpaRepository<RolepermissionEntity, RolepermissionId>,QuerydslPredicateExecutor<RolepermissionEntity> {

	   
}
