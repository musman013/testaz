package com.nfinity.ll.testaz.application.authorization.user.dto;

import java.util.Date;

public class GetRoleOutput {
    private Long id;
    private String displayName;
    private String name;
    private Long userId;
    private String userDescriptiveField;

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
  	
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

