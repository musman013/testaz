package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "specialties", schema = "sample")
public class SpecialtiesEntity implements Serializable {

  	private Integer id;
  	private String name;
 
  	public SpecialtiesEntity() {
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
  @Column(name = "name", nullable = true, length =80)
  public String getName() {
  return name;
  }

  public void setName(String name) {
  this.name = name;
  }
  
  @OneToMany(mappedBy = "specialties", cascade = CascadeType.ALL) 
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
//      if (!(o instanceof SpecialtiesEntity)) return false;
//        SpecialtiesEntity specialties = (SpecialtiesEntity) o;
//        return id != null && id.equals(specialties.id);
//  }

}

  
      


