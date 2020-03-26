package com.nfinity.ll.testaz.application.vetspecialties.dto;

import java.util.Date;
public class CreateVetSpecialtiesOutput {

    private Integer specialtyId;
    private Integer vetId;
	private String specialtiesDescriptiveField;
	private String vetsDescriptiveField;

  public String getSpecialtiesDescriptiveField() {
  	return specialtiesDescriptiveField;
  }

  public void setSpecialtiesDescriptiveField(String specialtiesDescriptiveField){
  	this.specialtiesDescriptiveField = specialtiesDescriptiveField;
  }
 
  public String getVetsDescriptiveField() {
  	return vetsDescriptiveField;
  }

  public void setVetsDescriptiveField(String vetsDescriptiveField){
  	this.vetsDescriptiveField = vetsDescriptiveField;
  }
 
  public Integer getSpecialtyId() {
  	return specialtyId;
  }

  public void setSpecialtyId(Integer specialtyId){
  	this.specialtyId = specialtyId;
  }
  
  public Integer getVetId() {
  	return vetId;
  }

  public void setVetId(Integer vetId){
  	this.vetId = vetId;
  }
  
}
