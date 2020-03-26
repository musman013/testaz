package com.nfinity.ll.testaz.domain.types;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.TypesEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.irepository.ITypesRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class TypesManager implements ITypesManager {

    @Autowired
    ITypesRepository  _typesRepository;
    
    @Autowired
	IPetsRepository  _petsRepository;
    
	public TypesEntity create(TypesEntity types) {

		return _typesRepository.save(types);
	}

	public void delete(TypesEntity types) {

		_typesRepository.delete(types);	
	}

	public TypesEntity update(TypesEntity types) {

		return _typesRepository.save(types);
	}

	public TypesEntity findById(Integer typesId) {
    	Optional<TypesEntity> dbTypes= _typesRepository.findById(typesId);
		if(dbTypes.isPresent()) {
			TypesEntity existingTypes = dbTypes.get();
		    return existingTypes;
		} else {
		    return null;
		}

	}

	public Page<TypesEntity> findAll(Predicate predicate, Pageable pageable) {

		return _typesRepository.findAll(predicate,pageable);
	}
}
