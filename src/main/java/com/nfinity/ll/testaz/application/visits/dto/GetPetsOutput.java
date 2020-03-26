package com.nfinity.ll.testaz.application.visits.dto;

import java.util.Date;

public class GetPetsOutput {
  private Date birthDate;
  private Integer id;
  private String name;

  private Integer visitsId;
  
  public Integer getVisitsId() {
  	return visitsId;
  }

  public void setVisitsId(Integer visitsId){
  	this.visitsId = visitsId;
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
