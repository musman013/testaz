package com.nfinity.ll.testaz.application.authorization.userrole.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateUserroleInput {

  @NotNull(message = "roleId Should not be null")
  private Long roleId;
  
  @NotNull(message = "user Id Should not be null")
  private Long userId;
  
  public Long getRoleId() {
  	return roleId;
  }

  public void setRoleId(Long roleId){
  	this.roleId = roleId;
  }
 
  public Long getUserId() {
  	return userId;
  }

  public void setUserId(Long userId){
  	this.userId = userId;
  }
 
}
