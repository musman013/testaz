package com.nfinity.ll.testaz.emailbuilder.application.emailtemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nfinity.ll.testaz.emailbuilder.domain.emailtemplate.IEmailTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfinity.ll.testaz.emailbuilder.application.emailtemplate.dto.*;
import com.nfinity.ll.testaz.emailbuilder.domain.model.EmailTemplateEntity;
import com.nfinity.ll.testaz.emailbuilder.domain.model.QEmailTemplateEntity;
import com.nfinity.ll.testaz.commons.search.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.request.Request;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.response.Response;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.service.MjmlOwnService;

@Servicepublic class EmailTemplateAppService implements IEmailTemplateAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IEmailTemplateManager _emailTemplateManager;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private EmailTemplateMapper emailTemplateMapper;
	
	@Autowired
	private Environment env;
	
	@Autowired
  private MjmlOwnService mjmlOwnService;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateEmailTemplateOutput create(CreateEmailTemplateInput email) {

		EmailTemplateEntity re = emailTemplateMapper.createEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity createdEmail = _emailTemplateManager.create(re);

		return emailTemplateMapper.emailTemplateEntityToCreateEmailTemplateOutput(createdEmail);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long eid) {
		EmailTemplateEntity existing = _emailTemplateManager.findById(eid);
		_emailTemplateManager.delete(existing);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateEmailTemplateOutput update(Long eid, UpdateEmailTemplateInput email) {

		EmailTemplateEntity ue = emailTemplateMapper.updateEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity updatedEmail = _emailTemplateManager.update(ue);

		return emailTemplateMapper.emailTemplateEntityToUpdateEmailTemplateOutput(updatedEmail);

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByIdOutput findById(Long eid) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.findById(eid);

		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a id=%s", eid);
			return null;
		}

		return emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByNameOutput findByName(String name) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.findByName(name);
		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a name=%s", name);
			return null;
		}
		return emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByNameOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindEmailTemplateByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception {
		Page<EmailTemplateEntity> foundEmail = _emailTemplateManager.findAll(search(search),pageable);
		List<EmailTemplateEntity> emailList = foundEmail.getContent();

		Iterator<EmailTemplateEntity> emailIterator = emailList.iterator();
		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();

		while (emailIterator.hasNext()) {
			output.add(emailTemplateMapper.emailTemplateEntityToFindEmailTemplateByIdOutput(emailIterator.next()));    
		}

		return output;
	}

	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllProperties(emailTemplate, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(emailTemplate,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(emailTemplate, map);
			}

		}
		return null;
	}

	public BooleanBuilder searchAllProperties(QEmailTemplateEntity emailTemplate,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(emailTemplate.templateName.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.category.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.to.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.cc.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.bcc.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.subject.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals")) {
			builder.or(emailTemplate.templateName.eq(value));
			builder.or(emailTemplate.category.eq(value));
			builder.or(emailTemplate.to.eq(value));
			builder.or(emailTemplate.cc.eq(value));
			builder.or(emailTemplate.bcc.eq(value));
			builder.or(emailTemplate.subject.eq(value));
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("templateName")) 
					|| (list.get(i).replace("%20","").trim().equals("category"))
					|| (list.get(i).replace("%20","").trim().equals("to"))
					|| (list.get(i).replace("%20","").trim().equals("cc"))
					|| (list.get(i).replace("%20","").trim().equals("bcc"))
					|| (list.get(i).replace("%20","").trim().equals("subject")))) {

				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchSpecificProperty(QEmailTemplateEntity emailTemplate,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("templateName")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.templateName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.templateName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("category")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.category.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.category.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("to")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.to.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.to.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("cc")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.cc.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.cc.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("bcc")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.bcc.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.bcc.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("subject")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.subject.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.subject.eq(value));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePair(QEmailTemplateEntity emailTemplate, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("templateName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.templateName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.templateName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.templateName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("category")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.category.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.category.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.category.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("to")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.to.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.to.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.to.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("cc")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.cc.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.cc.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.cc.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("bcc")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.bcc.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.bcc.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.bcc.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("subject")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.subject.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.subject.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.subject.ne(details.getValue().getSearchValue()));
				}
			}
			
		}
		return builder;
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
		= new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}

	public String convertJsonToHtml(String jsonString) throws IOException{
		String html= " ";
    ObjectMapper mapper = new ObjectMapper();
    Request request = mapper.readValue(jsonString, Request.class);
    Response response = mjmlOwnService.genrateHtml(request);
    logHelper.getLogger().error("Error",response.getErrors());
    html = response.getHtml();
    
    return html;
	}

}