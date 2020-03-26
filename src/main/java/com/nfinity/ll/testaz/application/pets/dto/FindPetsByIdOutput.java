package com.nfinity.ll.testaz.application.pets.dto;

import java.util.Date;
public class FindPetsByIdOutput {

  private Date birthDate;
  private Integer id;
  private String name;
  private Integer typeId;
  private String typesDescriptiveField;
  private Integer ownerId;
  private String ownersDescriptiveField;

  public Integer getTypeId() {
  	return typeId;
  }

  public void setTypeId(Integer typeId){
  	this.typeId = typeId;
  }
  
  public String getTypesDescriptiveField() {
  	return typesDescriptiveField;
  }

  public void setTypesDescriptiveField(String typesDescriptiveField){
  	this.typesDescriptiveField = typesDescriptiveField;
  }
 
  public Integer getOwnerId() {
  	return ownerId;
  }

  public void setOwnerId(Integer ownerId){
  	this.ownerId = ownerId;
  }
  
  public String getOwnersDescriptiveField() {
  	return ownersDescriptiveField;
  }

  public void setOwnersDescriptiveField(String ownersDescriptiveField){
  	this.ownersDescriptiveField = ownersDescriptiveField;
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
