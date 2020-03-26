package com.nfinity.ll.testaz.domain.authorization.rolepermission;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.RolepermissionEntity;
import com.nfinity.ll.testaz.domain.model.RolepermissionId;
import com.nfinity.ll.testaz.domain.irepository.IPermissionRepository;
import com.nfinity.ll.testaz.domain.model.PermissionEntity;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.model.RoleEntity;

import com.nfinity.ll.testaz.domain.irepository.IRolepermissionRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class RolepermissionManager implements IRolepermissionManager {

    @Autowired
    IRolepermissionRepository  _rolepermissionRepository;
    
    @Autowired
	IPermissionRepository  _permissionRepository;
    
    @Autowired
	IRoleRepository  _roleRepository;
    
	public RolepermissionEntity create(RolepermissionEntity rolepermission) {

		return _rolepermissionRepository.save(rolepermission);
	}

	public void delete(RolepermissionEntity rolepermission) {

		_rolepermissionRepository.delete(rolepermission);	
	}

	public RolepermissionEntity update(RolepermissionEntity rolepermission) {

		return _rolepermissionRepository.save(rolepermission);
	}

	public RolepermissionEntity findById(RolepermissionId rolepermissionId ) {
	
    Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission;
		} else {
		    return null;
		}

	}

	public Page<RolepermissionEntity> findAll(Predicate predicate, Pageable pageable) {

		return _rolepermissionRepository.findAll(predicate,pageable);
	}

    //Permission
	public PermissionEntity getPermission(RolepermissionId rolepermissionId) {
		
		Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission.getPermission();
		} else {
		    return null;
		}

	}
	
    //Role
	public RoleEntity getRole(RolepermissionId rolepermissionId) {
		
		Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission.getRole();
		} else {
		    return null;
		}

	}
	
}
