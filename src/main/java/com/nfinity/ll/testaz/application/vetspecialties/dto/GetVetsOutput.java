package com.nfinity.ll.testaz.application.vetspecialties.dto;

import java.util.Date;

public class GetVetsOutput {
  private String firstName;
  private Integer id;
  private String lastName;

  private Integer vetSpecialtiesSpecialtyId;
  
  public Integer getVetSpecialtiesSpecialtyId() {
  	return vetSpecialtiesSpecialtyId;
  }

  public void setVetSpecialtiesSpecialtyId(Integer vetSpecialtiesSpecialtyId){
  	this.vetSpecialtiesSpecialtyId = vetSpecialtiesSpecialtyId;
  }
  private Integer vetSpecialtiesVetId;
  
  public Integer getVetSpecialtiesVetId() {
  	return vetSpecialtiesVetId;
  }

  public void setVetSpecialtiesVetId(Integer vetSpecialtiesVetId){
  	this.vetSpecialtiesVetId = vetSpecialtiesVetId;
  }
  public String getFirstName() {
  	return firstName;
  }

  public void setFirstName(String firstName){
  	this.firstName = firstName;
  }
  public Integer getId() {
  	return id;
  }

  public void setId(Integer id){
  	this.id = id;
  }
  public String getLastName() {
  	return lastName;
  }

  public void setLastName(String lastName){
  	this.lastName = lastName;
  }

}
