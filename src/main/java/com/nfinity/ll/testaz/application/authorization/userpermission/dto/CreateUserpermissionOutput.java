package com.nfinity.ll.testaz.application.authorization.userpermission.dto;

public class CreateUserpermissionOutput {

  	private Long permissionId;
    private Long userId;
    private String userDescriptiveField;
  	private String permissionDescriptiveField;
  	private Boolean revoked;
    
    public Boolean getRevoked() {
    	return revoked;
    }

    public void setRevoked(Boolean revoked) {
    	this.revoked = revoked;
    }
  
  	public String getPermissionDescriptiveField() {
   		return permissionDescriptiveField;
  	}

  	public void setPermissionDescriptiveField(String permissionDescriptiveField){
   		this.permissionDescriptiveField = permissionDescriptiveField;
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
  	
  	public String getUserDescriptiveField() {
   	  	return userDescriptiveField;
  	}

  	public void setUserDescriptiveField(String userDescriptiveField){
   	  	this.userDescriptiveField = userDescriptiveField;
  	}
}
