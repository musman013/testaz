package com.nfinity.ll.testaz.application.vetspecialties.dto;

import java.util.Date;

public class GetSpecialtiesOutput {
  private Integer id;
  private String name;

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
