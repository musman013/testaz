package com.nfinity.ll.testaz.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "types", schema = "sample")
public class TypesEntity implements Serializable {

  	private Integer id;
  	private String name;
 
  	public TypesEntity() {
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
  
  @OneToMany(mappedBy = "types", cascade = CascadeType.ALL) 
  public Set<PetsEntity> getPetsSet() { 
      return petsSet; 
  } 
 
  public void setPetsSet(Set<PetsEntity> pets) { 
      this.petsSet = pets; 
  } 
 
  private Set<PetsEntity> petsSet = new HashSet<PetsEntity>(); 

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof TypesEntity)) return false;
//        TypesEntity types = (TypesEntity) o;
//        return id != null && id.equals(types.id);
//  }

}

  
      


