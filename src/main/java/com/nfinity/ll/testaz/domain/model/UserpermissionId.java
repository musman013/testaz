package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;

public class UserpermissionId implements Serializable {

    private Long permissionId;
    private Long userId;
    
    public UserpermissionId() {
    }

    public UserpermissionId(Long permissionId,Long userId){
  		this.permissionId =permissionId;
   		this.userId =userId;
    }
    
    public Long getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(Long permissionId){
        this.permissionId = permissionId;
    }
  	public Long getUserId() {
  		return userId;
  	}

  	public void setUserId(Long userId){
  		this.userId = userId;
  	}
    
}