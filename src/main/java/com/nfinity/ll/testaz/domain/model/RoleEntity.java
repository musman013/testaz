package com.nfinity.ll.testaz.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Role", schema = "sample")
public class RoleEntity implements Serializable {

    private Long id;
    private String displayName;
    private String name;

    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "DisplayName", nullable = false, length = 128)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "Name", nullable = false, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity)) return false;
        RoleEntity role = (RoleEntity) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
    public Set<RolepermissionEntity> getRolepermissionSet() { 
      return rolepermissionSet; 
    } 
 
    public void setRolepermissionSet(Set<RolepermissionEntity> rolepermission) { 
      this.rolepermissionSet = rolepermission; 
    } 
 
    private Set<RolepermissionEntity> rolepermissionSet = new HashSet<RolepermissionEntity>(); 
  
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
    public Set<UserroleEntity> getUserroleSet() { 
      return userroleSet; 
    } 
 
    public void setUserroleSet(Set<UserroleEntity> userrole) { 
      this.userroleSet = userrole; 
    } 
 
    private Set<UserroleEntity> userroleSet = new HashSet<UserroleEntity>(); 
    public RoleEntity() {

    }

}
