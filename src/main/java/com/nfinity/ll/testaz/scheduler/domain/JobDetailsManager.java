package com.nfinity.ll.testaz.scheduler.domain;

import com.nfinity.ll.testaz.scheduler.domain.irepository.IJobDetailsRepository;
import com.nfinity.ll.testaz.scheduler.domain.model.JobDetailsEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JobDetailsManager {

	 @Autowired
	    private IJobDetailsRepository _jobRepository;

	 @Transactional
	    public Page<JobDetailsEntity> findAll(Predicate predicate,Pageable pageable) {
	        return _jobRepository.findAll(predicate, pageable);
	    }


}
