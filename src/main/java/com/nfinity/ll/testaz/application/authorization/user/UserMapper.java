package com.nfinity.ll.testaz.application.authorization.user;

import com.nfinity.ll.testaz.application.authorization.user.dto.*;
import com.nfinity.ll.testaz.domain.model.UserEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /*
    CreateUserInput => User
    User => CreateUserOutput
    UpdateUserInput => User
    User => UpdateUserOutput
    User => FindUserByIdOutput
    Permission => GetPermissionOutput
    Role => GetRoleOutput
     */

    UserEntity createUserInputToUserEntity(CreateUserInput userDto);
   
    CreateUserOutput userEntityToCreateUserOutput(UserEntity entity);

    UserEntity updateUserInputToUserEntity(UpdateUserInput userDto);

    UpdateUserOutput userEntityToUpdateUserOutput(UserEntity entity);

    FindUserByIdOutput userEntityToFindUserByIdOutput(UserEntity entity);
     
    FindUserByNameOutput userEntityToFindUserByNameOutput(UserEntity entity);

    FindUserWithAllFieldsByIdOutput userEntityToFindUserWithAllFieldsByIdOutput(UserEntity entity);
  
}