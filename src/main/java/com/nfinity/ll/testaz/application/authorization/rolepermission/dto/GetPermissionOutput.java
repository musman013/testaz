package com.nfinity.ll.testaz.application.authorization.rolepermission.dto;

public class GetPermissionOutput {
  private String displayName;
  private Long id;
  private String name;
  private Long rolepermissionPermissionId;
  
  public Long getrolepermissionPermissionId() {
  return rolepermissionPermissionId;
  }

  public void setrolepermissionPermissionId(Long rolepermissionPermissionId){
  this.rolepermissionPermissionId = rolepermissionPermissionId;
  }
  private Long rolepermissionRoleId;
  
  public Long getrolepermissionRoleId() {
  return rolepermissionRoleId;
  }

  public void setrolepermissionRoleId(Long rolepermissionRoleId){
  this.rolepermissionRoleId = rolepermissionRoleId;
  }
  public String getDisplayName() {
  return displayName;
  }

  public void setDisplayName(String displayName){
  this.displayName = displayName;
  }
  public Long getId() {
  return id;
  }

  public void setId(Long id){
  this.id = id;
  }
  public String getName() {
  return name;
  }

  public void setName(String name){
  this.name = name;
  }

}
