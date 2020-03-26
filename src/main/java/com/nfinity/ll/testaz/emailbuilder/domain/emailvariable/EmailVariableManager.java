package com.nfinity.ll.testaz.emailbuilder.domain.emailvariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfinity.ll.testaz.emailbuilder.domain.irepository.IEmailVariableRepository;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailVariableEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class EmailVariableManager implements IEmailVariableManager {

	@Autowired
	IEmailVariableRepository  _emailVariableRepository;

	public EmailVariableEntity create(EmailVariableEntity email) {
		
		return _emailVariableRepository.save(email);
	}

	public void delete(EmailVariableEntity email) {
		
		_emailVariableRepository.delete(email);	
	}

	public EmailVariableEntity update(EmailVariableEntity email) {
		
		return _emailVariableRepository.save(email);
	}

	public EmailVariableEntity findById(Long emailId) {
		
		return _emailVariableRepository.findById(emailId.longValue());
	}

	public EmailVariableEntity findByName(String name) {
		
		return _emailVariableRepository.findByEmailName(name);
	}

	public Page<EmailVariableEntity> findAll(Predicate predicate,Pageable pageable) {
		
		return _emailVariableRepository.findAll(predicate,pageable);
	}
	
	

}
