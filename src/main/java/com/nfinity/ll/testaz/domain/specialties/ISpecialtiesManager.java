package com.nfinity.ll.testaz.domain.specialties;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.ll.testaz.domain.model.SpecialtiesEntity;
import com.nfinity.ll.testaz.domain.model.VetSpecialtiesEntity;

public interface ISpecialtiesManager {
    // CRUD Operations
    SpecialtiesEntity create(SpecialtiesEntity specialties);

    void delete(SpecialtiesEntity specialties);

    SpecialtiesEntity update(SpecialtiesEntity specialties);

    SpecialtiesEntity findById(Integer id);
	
    Page<SpecialtiesEntity> findAll(Predicate predicate, Pageable pageable);
}
