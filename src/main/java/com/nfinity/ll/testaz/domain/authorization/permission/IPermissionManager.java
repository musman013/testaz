package com.nfinity.ll.testaz.domain.authorization.permission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;

public interface IPermissionManager {
    // CRUD Operations
    PermissionEntity create(PermissionEntity user);

    void delete(PermissionEntity user);

    PermissionEntity update(PermissionEntity user);

    PermissionEntity findById(Long permissionId);

    Page<PermissionEntity> findAll(Predicate predicate, Pageable pageable);

    //Internal operation
    PermissionEntity findByPermissionName(String permissionName);
}
