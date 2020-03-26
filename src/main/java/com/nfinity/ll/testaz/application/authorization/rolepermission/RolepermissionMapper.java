package com.nfinity.ll.testaz.application.authorization.rolepermission;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.application.authorization.rolepermission.dto.*;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;


@Mapper(componentModel = "spring")
public interface RolepermissionMapper {

   RolepermissionEntity createRolepermissionInputToRolepermissionEntity(CreateRolepermissionInput rolepermissionDto);
   
   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   CreateRolepermissionOutput rolepermissionEntityToCreateRolepermissionOutput(RolepermissionEntity entity);

    RolepermissionEntity updateRolepermissionInputToRolepermissionEntity(UpdateRolepermissionInput rolepermissionDto);

   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   UpdateRolepermissionOutput rolepermissionEntityToUpdateRolepermissionOutput(RolepermissionEntity entity);

   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   FindRolepermissionByIdOutput rolepermissionEntityToFindRolepermissionByIdOutput(RolepermissionEntity entity);


   @Mappings({
   @Mapping(source = "rolepermission.permissionId", target = "rolepermissionPermissionId"),
   @Mapping(source = "rolepermission.roleId", target = "rolepermissionRoleId"),
   })
   GetPermissionOutput permissionEntityToGetPermissionOutput(PermissionEntity permission, RolepermissionEntity rolepermission);
 

   @Mappings({
   @Mapping(source = "rolepermission.permissionId", target = "rolepermissionPermissionId"),
   @Mapping(source = "rolepermission.roleId", target = "rolepermissionRoleId"),
   })
   GetRoleOutput roleEntityToGetRoleOutput(RoleEntity role, RolepermissionEntity rolepermission);
 

}
