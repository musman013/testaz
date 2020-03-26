package com.nfinity.ll.testaz.emailbuilder.domain.emailtemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfinity.ll.testaz.emailbuilder.domain.irepository.IEmailTemplateRepository;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailTemplateEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class EmailTemplateManager implements IEmailTemplateManager {

	@Autowired
	IEmailTemplateRepository  _emailTemplateRepository;

	public EmailTemplateEntity create(EmailTemplateEntity email) {

		return _emailTemplateRepository.save(email);
	}

	public void delete(EmailTemplateEntity email) {

		_emailTemplateRepository.delete(email);	
	}

	public EmailTemplateEntity update(EmailTemplateEntity email) {

		return _emailTemplateRepository.save(email);
	}

	public EmailTemplateEntity findById(Long emailId) {

		return _emailTemplateRepository.findById(emailId.longValue());
	}

	public Page<EmailTemplateEntity> findAll(Predicate predicate, Pageable pageable) {

		return _emailTemplateRepository.findAll(predicate,pageable);
	}

	public EmailTemplateEntity findByName(String name) {

		return _emailTemplateRepository.findByEmailName(name);
	}

}