package com.nfinity.ll.testaz.application.authorization.userrole.dto;

import java.util.Date;

public class GetRoleOutput {
    
    private String displayName;
    private Long id;
    private String name;

    private Long userroleRoleId;
  
    public Long getUserroleRoleId() {
  		return userroleRoleId;
    }

    public void setUserroleRoleId(Long userroleRoleId){
  		this.userroleRoleId = userroleRoleId;
    }
  
    private Long userroleUserId;
  
    public Long getUserroleUserId() {
		return userroleUserId;
	}

    public void setUserroleUserId(Long userroleUserId){
   		this.userroleUserId = userroleUserId;
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
