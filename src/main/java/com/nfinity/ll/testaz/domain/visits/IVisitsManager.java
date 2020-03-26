package com.nfinity.ll.testaz.domain.visits;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.ll.testaz.domain.model.VisitsEntity;
import com.nfinity.ll.testaz.domain.model.PetsEntity;

public interface IVisitsManager {
    // CRUD Operations
    VisitsEntity create(VisitsEntity visits);

    void delete(VisitsEntity visits);

    VisitsEntity update(VisitsEntity visits);

    VisitsEntity findById(Integer id);
	
    Page<VisitsEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Pets
    public PetsEntity getPets(Integer visitsId);
}
