package com.nfinity.ll.testaz.application.pets.dto;

import java.util.Date;

public class GetTypesOutput {
  private Integer id;
  private String name;

  private Integer petsId;
  
  public Integer getPetsId() {
  	return petsId;
  }

  public void setPetsId(Integer petsId){
  	this.petsId = petsId;
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
