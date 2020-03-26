package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "vets", schema = "sample")
public class VetsEntity implements Serializable {

  	private String firstName;
  	private Integer id;
  	private String lastName;
 
  	public VetsEntity() {
  	}

  @Basic
  @Column(name = "firstName", nullable = true, length =30)
  public String getFirstName() {
  return firstName;
  }

  public void setFirstName(String firstName) {
  this.firstName = firstName;
  }
  
  	@Id
  	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name = "id", nullable = false)
  public Integer getId() {
  return id;
  }

  public void setId(Integer id) {
  this.id = id;
  } 
  
  @Basic
  @Column(name = "lastName", nullable = true, length =30)
  public String getLastName() {
  return lastName;
  }

  public void setLastName(String lastName) {
  this.lastName = lastName;
  }
  
  @OneToMany(mappedBy = "vets", cascade = CascadeType.ALL) 
  public Set<VetSpecialtiesEntity> getVetSpecialtiesSet() { 
      return vetspecialtiesSet; 
  } 
 
  public void setVetSpecialtiesSet(Set<VetSpecialtiesEntity> vetspecialties) { 
      this.vetspecialtiesSet = vetspecialties; 
  } 
 
  private Set<VetSpecialtiesEntity> vetspecialtiesSet = new HashSet<VetSpecialtiesEntity>(); 

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof VetsEntity)) return false;
//        VetsEntity vets = (VetsEntity) o;
//        return id != null && id.equals(vets.id);
//  }

}

  
      


