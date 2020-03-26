package com.nfinity.ll.testaz.domain.model;

import com.nfinity.ll.testaz.domain.model.RoleEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Permission", schema = "sample")

public class PermissionEntity implements Serializable {
    private Long id;
    private String name;
    private String displayName;

    public PermissionEntity() {
    }
    
    public PermissionEntity(String name, String displayName) {
    	this.name = name;
    	this.displayName = displayName;
    }

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
    @Column(name = "Name", nullable = false, length = 128,unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionEntity)) return false;
        PermissionEntity permission = (PermissionEntity) o;
        return id != null && id.equals(permission.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL) 
    public Set<RolepermissionEntity> getRolepermissionSet() { 
      return rolepermissionSet; 
    } 
 
    public void setRolepermissionSet(Set<RolepermissionEntity> rolepermission) { 
      this.rolepermissionSet = rolepermission; 
    } 
 
    private Set<RolepermissionEntity> rolepermissionSet = new HashSet<RolepermissionEntity>(); 
  
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL) 
    public Set<UserpermissionEntity> getUserpermissionSet() { 
      return userpermissionSet; 
    } 
 
    public void setUserpermissionSet(Set<UserpermissionEntity> userpermission) { 
      this.userpermissionSet = userpermission; 
    } 
 
    private Set<UserpermissionEntity> userpermissionSet = new HashSet<UserpermissionEntity>(); 

}
