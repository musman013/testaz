package com.nfinity.ll.testaz.application.vetspecialties.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateVetSpecialtiesInput {

  @NotNull(message = "specialtyId Should not be null")
  private Integer specialtyId;
  @NotNull(message = "vetId Should not be null")
  private Integer vetId;

 
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
