package com.nfinity.ll.testaz;

import com.nfinity.ll.testaz.domain.model.*;
import com.nfinity.ll.testaz.domain.authorization.permission.IPermissionManager;
import com.nfinity.ll.testaz.domain.authorization.rolepermission.IRolepermissionManager;
import com.nfinity.ll.testaz.domain.authorization.userrole.IUserroleManager;
import com.nfinity.ll.testaz.domain.authorization.user.IUserManager;
import com.nfinity.ll.testaz.domain.authorization.role.IRoleManager;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("bootstrap")
public class AppStartupRunner implements ApplicationRunner {
	
	@Autowired
	private Environment environment;
	 
    @Autowired
    private IPermissionManager permissionManager;
    
    @Autowired
    private IRoleManager roleManager;

    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private IUserroleManager userroleManager;
    
	@Autowired
    private IRolepermissionManager rolepermissionManager;
    
    @Autowired
    private LoggingHelper loggingHelper;
 
    @Autowired
    private PasswordEncoder pEncoder;

    @Override
    public void run(ApplicationArguments args) {
    
     System.out.println("*****************Creating Default Users/Roles/Permissions *************************");

        // Create permissions for default entities

        loggingHelper.getLogger().info("Creating the data in the database");

        // Create roles

        RoleEntity role = new RoleEntity();
        role.setName("ROLE_Admin");
        role.setDisplayName("Role1");
        role = roleManager.create(role);
        addDefaultUser(role);
        
		List<String> entityList = new ArrayList<String>();
        entityList.add("role");
        entityList.add("permission");
        entityList.add("rolepermission");

		entityList.add("user");
		entityList.add("userpermission");
		entityList.add("userrole");
        entityList.add("email");
        entityList.add("emailVariable");
        entityList.add("jobDetails");
        entityList.add("triggerDetails");

		entityList.add("types");
		entityList.add("pets");
		entityList.add("specialties");
		entityList.add("visits");
		entityList.add("vets");
		entityList.add("vetspecialties");
		entityList.add("owners");
		
		for(String entity: entityList) {
			if(!environment.getProperty("fastCode.auth.method").equals("database") && entity.equals("user"))
        		addEntityPermissions(entity, role.getId(), true);
			else
				addEntityPermissions(entity, role.getId(), false);
        }
        
      	addEntityHistoryPermissions("entityHistory", role.getId());
        loggingHelper.getLogger().info("Completed creating the data in the database");

    }
    
    private void addEntityHistoryPermissions(String entity, long roleId) {
    	PermissionEntity pe = new PermissionEntity(entity.toUpperCase() , entity);
		pe = permissionManager.create(pe);
		RolepermissionEntity pe2RP = new RolepermissionEntity(pe.getId(), roleId);
		rolepermissionManager.create(pe2RP);
    }
    
    private void addEntityPermissions(String entity, long roleId,boolean readOnly) {
    	if(readOnly)
    	{
    		PermissionEntity pe2 = new PermissionEntity(entity.toUpperCase() + "ENTITY_READ", "read " + entity);
    		pe2 = permissionManager.create(pe2);
    		RolepermissionEntity pe2RP = new RolepermissionEntity(pe2.getId(), roleId);
    		rolepermissionManager.create(pe2RP);
    	}
    	else
    	{
		PermissionEntity pe1 = new PermissionEntity(entity.toUpperCase() + "ENTITY_CREATE", "create " + entity);
    	PermissionEntity pe2 = new PermissionEntity(entity.toUpperCase() + "ENTITY_READ", "read " + entity);
        PermissionEntity pe3 = new PermissionEntity(entity.toUpperCase() + "ENTITY_DELETE", "delete " + entity);
        PermissionEntity pe4 = new PermissionEntity(entity.toUpperCase() + "ENTITY_UPDATE", "update " + entity);
    	

        pe1 = permissionManager.create(pe1);
        pe2 = permissionManager.create(pe2);
        pe3 = permissionManager.create(pe3);
        pe4 = permissionManager.create(pe4);
        
        RolepermissionEntity pe1RP = new RolepermissionEntity(pe1.getId(), roleId);
        RolepermissionEntity pe2RP = new RolepermissionEntity(pe2.getId(), roleId);
        RolepermissionEntity pe3RP = new RolepermissionEntity(pe3.getId(), roleId);
        RolepermissionEntity pe4RP = new RolepermissionEntity(pe4.getId(), roleId);
        
        rolepermissionManager.create(pe1RP);
        rolepermissionManager.create(pe2RP);
        rolepermissionManager.create(pe3RP);
        rolepermissionManager.create(pe4RP);
    	}
    }
    
    private void addDefaultUser(RoleEntity role) {
    	UserEntity admin = new UserEntity();
        admin.setFirstName("test");
    	admin.setLastName("admin");
    	admin.setUserName("admin");
    	admin.setEmailAddress("admin@demo.com");
    	admin.setPassword(pEncoder.encode("secret"));
    	admin.setIsActive(true);
    	admin = userManager.create(admin);
    	UserroleEntity urole = new UserroleEntity();
    	urole.setRoleId(role.getId());
    	urole.setUserId(admin.getId());
		urole=userroleManager.create(urole);
    }
}
