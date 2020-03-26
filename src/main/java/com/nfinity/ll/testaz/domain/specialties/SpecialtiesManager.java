package com.nfinity.ll.testaz.domain.specialties;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.irepository.IVetSpecialtiesRepository;
import com.nfinity.ll.testaz.domain.irepository.ISpecialtiesRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class SpecialtiesManager implements ISpecialtiesManager {

    @Autowired
    ISpecialtiesRepository  _specialtiesRepository;
    
    @Autowired
	IVetSpecialtiesRepository  _vetspecialtiesRepository;
    
	public SpecialtiesEntity create(SpecialtiesEntity specialties) {

		return _specialtiesRepository.save(specialties);
	}

	public void delete(SpecialtiesEntity specialties) {

		_specialtiesRepository.delete(specialties);	
	}

	public SpecialtiesEntity update(SpecialtiesEntity specialties) {

		return _specialtiesRepository.save(specialties);
	}

	public SpecialtiesEntity findById(Integer specialtiesId) {
    	Optional<SpecialtiesEntity> dbSpecialties= _specialtiesRepository.findById(specialtiesId);
		if(dbSpecialties.isPresent()) {
			SpecialtiesEntity existingSpecialties = dbSpecialties.get();
		    return existingSpecialties;
		} else {
		    return null;
		}

	}

	public Page<SpecialtiesEntity> findAll(Predicate predicate, Pageable pageable) {

		return _specialtiesRepository.findAll(predicate,pageable);
	}
}
