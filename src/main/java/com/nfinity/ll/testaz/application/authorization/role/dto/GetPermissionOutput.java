package com.nfinity.ll.testaz.application.authorization.role.dto;

public class GetPermissionOutput {

    private Long id;
    private String displayName;
    private String name;
    private Long roleId;
    private String roleDescriptiveField;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleDescriptiveField() {
        return roleDescriptiveField;
    }

    public void setRoleDescriptiveField(String roleDescriptiveField) {
        this.roleDescriptiveField = roleDescriptiveField;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
