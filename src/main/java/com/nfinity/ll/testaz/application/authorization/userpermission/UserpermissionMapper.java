package com.nfinity.ll.testaz.application.authorization.userpermission;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.application.authorization.userpermission.dto.*;
import com.nfinity.ll.testaz.domain.model.UserpermissionEntity;


@Mapper(componentModel = "spring")
public interface UserpermissionMapper {

   UserpermissionEntity createUserpermissionInputToUserpermissionEntity(CreateUserpermissionInput userpermissionDto);
   
   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                   
   }) 
   CreateUserpermissionOutput userpermissionEntityToCreateUserpermissionOutput(UserpermissionEntity entity);

   UserpermissionEntity updateUserpermissionInputToUserpermissionEntity(UpdateUserpermissionInput userpermissionDto);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                  
   }) 
   UpdateUserpermissionOutput userpermissionEntityToUpdateUserpermissionOutput(UserpermissionEntity entity);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                  
   }) 
   FindUserpermissionByIdOutput userpermissionEntityToFindUserpermissionByIdOutput(UserpermissionEntity entity);


   @Mappings({
   @Mapping(source = "userpermission.permissionId", target = "userpermissionPermissionId"),
   @Mapping(source = "userpermission.userId", target = "userpermissionUserId")
   })
   GetUserOutput userEntityToGetUserOutput(UserEntity user, UserpermissionEntity userpermission);
 

   @Mappings({
   @Mapping(source = "userpermission.userId", target = "userpermissionUserId"),
   @Mapping(source = "userpermission.permissionId", target = "userpermissionPermissionId"),
   @Mapping(source = "permission.id", target = "id")
   })
   GetPermissionOutput permissionEntityToGetPermissionOutput(PermissionEntity permission, UserpermissionEntity userpermission);
 

}
