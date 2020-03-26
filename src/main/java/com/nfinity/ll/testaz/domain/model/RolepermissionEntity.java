package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "Rolepermission", schema = "sample")
@IdClass(RolepermissionId.class)
public class RolepermissionEntity implements Serializable {

  private Long permissionId;
  private Long roleId;
 
  public RolepermissionEntity() {
  }
  
  public RolepermissionEntity(Long permissionId, Long roleId) {
	  this.roleId = roleId;
	  this.permissionId = permissionId;
  }

  @ManyToOne
  @JoinColumn(name = "permissionId", insertable=false, updatable=false)
  public PermissionEntity getPermission() {
    return permission;
  }
  public void setPermission(PermissionEntity permission) {
    this.permission = permission;
  }
  
  private PermissionEntity permission;
 
  @Id
  @Column(name = "permissionId", nullable = false)
  public Long getPermissionId() {
  return permissionId;
  }

  public void setPermissionId(Long permissionId){
  this.permissionId = permissionId;
  }
  
  @ManyToOne
  @JoinColumn(name = "roleId", insertable=false, updatable=false)
  public RoleEntity getRole() {
    return role;
  }
  public void setRole(RoleEntity role) {
    this.role = role;
  }
  
  private RoleEntity role;
 
  @Id
  @Column(name = "roleId", nullable = false)
  public Long getRoleId() {
  return roleId;
  }

  public void setRoleId(Long roleId){
  this.roleId = roleId;
  }
  
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof RolepermissionEntity)) return false;
//        RolepermissionEntity rolepermission = (RolepermissionEntity) o;
//        return id != null && id.equals(rolepermission.id);
//  }

}

  
      


