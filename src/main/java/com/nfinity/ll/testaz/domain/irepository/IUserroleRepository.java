package com.nfinity.ll.testaz.domain.irepository;

import com.nfinity.ll.testaz.domain.model.UserroleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "userrole", path = "userrole")
public interface IUserroleRepository extends JpaRepository<UserroleEntity, UserroleId>,QuerydslPredicateExecutor<UserroleEntity> {
//    @Query("select e from UserroleEntity e where e.roleId = ?1 and e.userId = ?2")
//	UserroleEntity findById(Long roleId,Long userId);

}
