package com.nfinity.ll.testaz.domain.authorization.userrole;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.UserroleEntity;
import com.nfinity.ll.testaz.domain.model.UserroleId;
import com.nfinity.ll.testaz.domain.irepository.IUserRepository;
import com.nfinity.ll.testaz.domain.model.UserEntity;
import com.nfinity.ll.testaz.domain.irepository.IRoleRepository;
import com.nfinity.ll.testaz.domain.model.RoleEntity;
import com.nfinity.ll.testaz.domain.irepository.IUserroleRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class UserroleManager implements IUserroleManager {

    @Autowired
    IUserroleRepository  _userroleRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    @Autowired
	IRoleRepository  _roleRepository;
    
	public UserroleEntity create(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}

	public void delete(UserroleEntity userrole) {

		_userroleRepository.delete(userrole);	
	}

	public UserroleEntity update(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}

	public UserroleEntity findById(UserroleId userroleId) {
    	Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole;
		} else {
		    return null;
		}

	}

	public Page<UserroleEntity> findAll(Predicate predicate, Pageable pageable) {

		return _userroleRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getUser();
		} else {
		    return null;
		}
	}
  
   //Role
	public RoleEntity getRole(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getRole();
		} else {
		    return null;
		}
	}
}
