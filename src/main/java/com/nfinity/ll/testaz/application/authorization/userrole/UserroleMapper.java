package com.nfinity.ll.testaz.application.authorization.userrole;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.application.authorization.userrole.dto.*;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;

@Mapper(componentModel = "spring")
public interface UserroleMapper {

   UserroleEntity createUserroleInputToUserroleEntity(CreateUserroleInput userroleDto);
   
   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                   
   }) 
   CreateUserroleOutput userroleEntityToCreateUserroleOutput(UserroleEntity entity);

   UserroleEntity updateUserroleInputToUserroleEntity(UpdateUserroleInput userroleDto);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                  
   }) 
   UpdateUserroleOutput userroleEntityToUpdateUserroleOutput(UserroleEntity entity);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                  
   })
   FindUserroleByIdOutput userroleEntityToFindUserroleByIdOutput(UserroleEntity entity);

   @Mappings({
   @Mapping(source = "userrole.roleId", target = "userroleRoleId"),
   @Mapping(source = "userrole.userId", target = "userroleUserId")
   })
   GetUserOutput userEntityToGetUserOutput(UserEntity user, UserroleEntity userrole);
 
   @Mappings({
   @Mapping(source = "userrole.userId", target = "userroleUserId"),
   @Mapping(source = "userrole.roleId", target = "userroleRoleId"),
   @Mapping(source = "role.id", target = "id")
   })
   GetRoleOutput roleEntityToGetRoleOutput(RoleEntity role, UserroleEntity userrole);
 
}
