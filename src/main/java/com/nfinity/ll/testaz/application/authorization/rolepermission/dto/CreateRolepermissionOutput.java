package com.nfinity.ll.testaz.application.authorization.rolepermission.dto;

public class CreateRolepermissionOutput {

  private Long permissionId;
  private Long roleId;
  private String permissionDescriptiveField;
  private String roleDescriptiveField;
  
  public String getPermissionDescriptiveField() {
   return permissionDescriptiveField;
  }

  public void setPermissionDescriptiveField(String permissionDescriptiveField){
   this.permissionDescriptiveField = permissionDescriptiveField;
  }
  
  public String getRoleDescriptiveField() {
   return roleDescriptiveField;
  }

  public void setRoleDescriptiveField(String roleDescriptiveField){
   this.roleDescriptiveField = roleDescriptiveField;
  }
  
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
