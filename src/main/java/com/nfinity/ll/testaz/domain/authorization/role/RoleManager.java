package com.nfinity.ll.testaz.domain.authorization.role;

import java.util.Optional;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.irepository.IRolepermissionRepository;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class RoleManager implements IRoleManager {

    @Autowired
    IRoleRepository  _roleRepository;
    
    @Autowired
	IRolepermissionRepository  _rolepermissionRepository;
    
	public RoleEntity create(RoleEntity role) {

		return _roleRepository.save(role);
	}

	public void delete(RoleEntity role) {

		_roleRepository.delete(role);	
	}

	public RoleEntity update(RoleEntity role) {

		return _roleRepository.save(role);
	}
	
	// Internal Operations
	public RoleEntity findByRoleName(String roleName) {
		return _roleRepository.findByRoleName(roleName);
	}

	public RoleEntity findById(Long  roleId)
    {
    Optional<RoleEntity> dbRole= _roleRepository.findById(roleId);
		if(dbRole.isPresent()) {
			RoleEntity existingRole = dbRole.get();
		    return existingRole;
		} else {
		    return null;
		}
	}

	public Page<RoleEntity> findAll(Predicate predicate, Pageable pageable) {

		return _roleRepository.findAll(predicate,pageable);
	}

}
