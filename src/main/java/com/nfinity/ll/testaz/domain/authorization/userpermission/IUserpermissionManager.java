package com.nfinity.ll.testaz.domain.authorization.userpermission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;
import com.nfinity.ll.testaz.domain.model.UserpermissionId;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;

public interface IUserpermissionManager {

    // CRUD Operations
    UserpermissionEntity create(UserpermissionEntity userpermission);

    void delete(UserpermissionEntity userpermission);

    UserpermissionEntity update(UserpermissionEntity userpermission);

    UserpermissionEntity findById(UserpermissionId userpermissionId );

    Page<UserpermissionEntity> findAll(Predicate predicate, Pageable pageable);
   
    //User
    public UserEntity getUser(UserpermissionId userpermissionId );
  
    //Permission
    public PermissionEntity getPermission(UserpermissionId userpermissionId );
  
}
