package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Userrole", schema = "sample")
@IdClass(UserroleId.class)
public class UserroleEntity implements Serializable {

	private Long roleId;
  	private Long userId;
 
  	public UserroleEntity() {
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
  
  	@Id
  	@Column(name = "userId", nullable = false)
  	public Long getUserId() {
  		return userId;
  	}

  	public void setUserId(Long userId){
  		this.userId = userId;
  	}
  
  	@ManyToOne
  	@JoinColumn(name = "userId", insertable=false, updatable=false)
  	public UserEntity getUser() {
    	return user;
  	}
  	public void setUser(UserEntity user) {
    	this.user = user;
  	}
  
  	private UserEntity user;
  
}

  
      


