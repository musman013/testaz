package com.nfinity.ll.testaz.application.authorization.role;

import com.nfinity.ll.testaz.application.authorization.role.dto.*;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleEntity createRoleInputToRoleEntity(CreateRoleInput roleDto);

    CreateRoleOutput roleEntityToCreateRoleOutput(RoleEntity entity);

    RoleEntity updateRoleInputToRoleEntity(UpdateRoleInput roleDto);

    UpdateRoleOutput roleEntityToUpdateRoleOutput(RoleEntity entity);

    FindRoleByIdOutput roleEntityToFindRoleByIdOutput(RoleEntity entity);
    
    FindRoleByNameOutput roleEntityToFindRoleByNameOutput(RoleEntity entity);

}
