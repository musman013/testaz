package com.nfinity.ll.testaz.emailbuilder.domain.emailvariable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailVariableEntity;

import com.querydsl.core.types.Predicate;

public interface IEmailVariableManager {

	 // CRUD Operations
    public EmailVariableEntity create(EmailVariableEntity email);

    public void delete(EmailVariableEntity email);

    public EmailVariableEntity update(EmailVariableEntity email);

    public EmailVariableEntity findById(Long emailId);
    
    public EmailVariableEntity findByName (String name);

    public Page<EmailVariableEntity> findAll(Predicate predicate,Pageable pageable);
	
	
}
