package com.nfinity.ll.testaz.application.authorization.userrole.dto;

import java.util.Date;
public class FindUserroleByIdOutput {

    private Long roleId;
    private Long userId;
    private String userDescriptiveField;
    private String roleDescriptiveField;

    public String getRoleDescriptiveField() {
  		return roleDescriptiveField;
    }

    public void setRoleDescriptiveField(String roleDescriptiveField){
  		this.roleDescriptiveField = roleDescriptiveField;
    }
 
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
  	
  	public String getUserDescriptiveField() {
   	  	return userDescriptiveField;
  	}

  	public void setUserDescriptiveField(String userDescriptiveField){
   	  	this.userDescriptiveField = userDescriptiveField;
  	}
}
