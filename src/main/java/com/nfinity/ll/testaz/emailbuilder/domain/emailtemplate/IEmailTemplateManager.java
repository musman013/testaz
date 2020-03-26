package com.nfinity.ll.testaz.emailbuilder.domain.emailtemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailTemplateEntity;

import com.querydsl.core.types.Predicate;

public interface IEmailTemplateManager {

	 // CRUD Operations
    public EmailTemplateEntity create(EmailTemplateEntity email);

    public void delete(EmailTemplateEntity email);

    public EmailTemplateEntity update(EmailTemplateEntity email);

    public EmailTemplateEntity findById(Long emailId);
    
    public EmailTemplateEntity findByName (String name);

    public Page<EmailTemplateEntity> findAll(Predicate predicate,Pageable pageable);
	
}
