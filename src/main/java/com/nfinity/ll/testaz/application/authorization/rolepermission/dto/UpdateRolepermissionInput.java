package com.nfinity.ll.testaz.application.authorization.rolepermission.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateRolepermissionInput {

  @NotNull(message = "permissionId Should not be null")
  private Long permissionId;
  
  @NotNull(message = "roleId Should not be null")
  private Long roleId;
  
  public Long getPermissionId() {
  return permissionId;
  }

  public void setPermissionId(Long permissionId){
  this.permissionId = permissionId;
  }
  public Long getRoleId() {
  return roleId;
  }

  public void setRoleId(Long roleId){
  this.roleId = roleId;
  }
 
}
