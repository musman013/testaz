package com.nfinity.ll.testaz.application.authorization.permission;

import com.nfinity.ll.testaz.application.authorization.permission.dto.*;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

     PermissionEntity createPermissionInputToPermissionEntity(CreatePermissionInput permissionDto);
   
     CreatePermissionOutput permissionEntityToCreatePermissionOutput(PermissionEntity entity);

     PermissionEntity updatePermissionInputToPermissionEntity(UpdatePermissionInput permissionDto);

     UpdatePermissionOutput permissionEntityToUpdatePermissionOutput(PermissionEntity entity);

     FindPermissionByIdOutput permissionEntityToFindPermissionByIdOutput(PermissionEntity entity);

     FindPermissionByNameOutput permissionEntityToFindPermissionByNameOutput(PermissionEntity entity);
   
}