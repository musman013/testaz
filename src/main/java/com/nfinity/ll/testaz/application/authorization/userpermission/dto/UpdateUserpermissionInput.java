package com.nfinity.ll.testaz.application.authorization.userpermission.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateUserpermissionInput {

    @NotNull(message = "permissionId Should not be null")
    private Long permissionId;
  
    @NotNull(message = "user Id Should not be null")
    private Long userId;
    private Boolean revoked;
    
    public Boolean getRevoked() {
    	return revoked;
    }

    public void setRevoked(Boolean revoked) {
    	this.revoked = revoked;
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
