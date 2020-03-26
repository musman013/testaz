package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;

public class UserroleId implements Serializable {

    private Long roleId;
    private Long userId;

    public UserroleId() {
    }

    public UserroleId(Long roleId,Long userId) {
  		this.roleId =roleId;
   		this.userId =userId;
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
    
}