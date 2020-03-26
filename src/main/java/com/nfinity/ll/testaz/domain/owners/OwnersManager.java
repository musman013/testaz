package com.nfinity.ll.testaz.domain.owners;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.OwnersEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.irepository.IOwnersRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class OwnersManager implements IOwnersManager {

    @Autowired
    IOwnersRepository  _ownersRepository;
    
    @Autowired
	IPetsRepository  _petsRepository;
    
	public OwnersEntity create(OwnersEntity owners) {

		return _ownersRepository.save(owners);
	}

	public void delete(OwnersEntity owners) {

		_ownersRepository.delete(owners);	
	}

	public OwnersEntity update(OwnersEntity owners) {

		return _ownersRepository.save(owners);
	}

	public OwnersEntity findById(Integer ownersId) {
    	Optional<OwnersEntity> dbOwners= _ownersRepository.findById(ownersId);
		if(dbOwners.isPresent()) {
			OwnersEntity existingOwners = dbOwners.get();
		    return existingOwners;
		} else {
		    return null;
		}

	}

	public Page<OwnersEntity> findAll(Predicate predicate, Pageable pageable) {

		return _ownersRepository.findAll(predicate,pageable);
	}
}
