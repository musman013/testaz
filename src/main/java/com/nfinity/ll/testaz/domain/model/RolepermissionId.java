package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;

public class RolepermissionId implements Serializable {

    private Long permissionId;
    private Long roleId;
    
    public RolepermissionId() {

    }
    public RolepermissionId(Long permissionId,Long roleId) {
  		this.permissionId =permissionId;
  		this.roleId =roleId;
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