package com.nfinity.ll.testaz.domain.visits;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.irepository.IPetsRepository;
import com.nfinity.ll.testaz.domain.model.PetsEntity;
import com.nfinity.ll.testaz.domain.irepository.IVisitsRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class VisitsManager implements IVisitsManager {

    @Autowired
    IVisitsRepository  _visitsRepository;
    
    @Autowired
	IPetsRepository  _petsRepository;
    
	public VisitsEntity create(VisitsEntity visits) {

		return _visitsRepository.save(visits);
	}

	public void delete(VisitsEntity visits) {

		_visitsRepository.delete(visits);	
	}

	public VisitsEntity update(VisitsEntity visits) {

		return _visitsRepository.save(visits);
	}

	public VisitsEntity findById(Integer visitsId) {
    	Optional<VisitsEntity> dbVisits= _visitsRepository.findById(visitsId);
		if(dbVisits.isPresent()) {
			VisitsEntity existingVisits = dbVisits.get();
		    return existingVisits;
		} else {
		    return null;
		}

	}

	public Page<VisitsEntity> findAll(Predicate predicate, Pageable pageable) {

		return _visitsRepository.findAll(predicate,pageable);
	}
  
   //Pets
	public PetsEntity getPets(Integer visitsId) {
		
		Optional<VisitsEntity> dbVisits= _visitsRepository.findById(visitsId);
		if(dbVisits.isPresent()) {
			VisitsEntity existingVisits = dbVisits.get();
		    return existingVisits.getPets();
		} else {
		    return null;
		}
	}
}
