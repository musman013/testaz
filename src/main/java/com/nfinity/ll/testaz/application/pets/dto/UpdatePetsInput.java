package com.nfinity.ll.testaz.application.pets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdatePetsInput {

  private Date birthDate;
  @NotNull(message = "id Should not be null")
  private Integer id;
  @Length(max = 30, message = "name must be less than 30 characters")
  private String name;
  private Integer typeId;
  private Integer ownerId;

  public Integer getTypeId() {
  	return typeId;
  }

  public void setTypeId(Integer typeId){
  	this.typeId = typeId;
  }
  public Integer getOwnerId() {
  	return ownerId;
  }

  public void setOwnerId(Integer ownerId){
  	this.ownerId = ownerId;
  }
 
  public Date getBirthDate() {
  	return birthDate;
  }

  public void setBirthDate(Date birthDate){
  	this.birthDate = birthDate;
  }
 
  public Integer getId() {
  	return id;
  }

  public void setId(Integer id){
  	this.id = id;
  }
 
  public String getName() {
  	return name;
  }

  public void setName(String name){
  	this.name = name;
  }
}
