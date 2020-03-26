package com.nfinity.ll.testaz.emailbuilder.application.emailtemplate;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.emailbuilder.application.emailtemplate.dto.*;

@Service
public interface IEmailTemplateAppService {

	CreateEmailTemplateOutput create(CreateEmailTemplateInput email);

    void delete(@Positive(message ="EmailId should be a positive value")Long eid);

    UpdateEmailTemplateOutput update(@Positive(message ="EmailId should be a positive value") Long eid,UpdateEmailTemplateInput email);

    FindEmailTemplateByIdOutput findById(@Positive(message ="EmailId should be a positive value")Long eid);

    FindEmailTemplateByNameOutput findByName(String name);
    
    List<FindEmailTemplateByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	
}
