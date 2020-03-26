package com.nfinity.ll.testaz.emailbuilder.application.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.nfinity.ll.testaz.emailbuilder.application.emailtemplate.dto.CreateEmailTemplateInput;
import com.nfinity.ll.testaz.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.nfinity.ll.testaz.emailbuilder.application.emailvariable.dto.FindEmailVariableByIdOutput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService implements IEmailService{

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	private Environment env;

	@Autowired
	private EmailTemplateAppService emailTemplateAppService;

	@Autowired
	private EmailVariableAppService emailVariableAppService;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void sendSimpleMessage(String to, String subject, String text)  {
		try {

			MimeMessage message = emailSender.createMimeMessage();

			//Set Email
			// message.setFrom("testemail@abc.com");

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text,true);
			
			
			emailSender.send(message);
		
		}
		catch (MessagingException ex) {
			ex.printStackTrace();
		}


	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void sendSimpleMessageUsingTemplate(String to, String subject, SimpleMailMessage template,
			String... templateArgs) {
		// TODO Auto-generated method stub

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
		try {

			MimeMessage message = emailSender.createMimeMessage();

			//Set Email
			// message.setFrom("testemail@abc.com");

			MimeMessageHelper helper = new MimeMessageHelper(message, true);


			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text,true);
			if(pathToAttachment != null) {
				FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
				helper.addAttachment(pathToAttachment, file);
			}


			emailSender.send(message);
		}
		catch (MessagingException ex) {
			ex.printStackTrace();
		}

	}

	private String replaceVariable(String input){
		//Sort sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")});
	   // Sort sort=new Sort (Sort.by("name"));
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(env.getProperty("fastCode.offset.default")), Integer.parseInt(env.getProperty("fastCode.limit.default")));
		
	//	Pageable pageable = new OffsetBasedPageRequest(0, 100);
		HashMap<String, String> map = new HashMap<>();

		List<FindEmailVariableByIdOutput> tags ;
		try
		{
			
			SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject("");
			tags = emailVariableAppService.find(searchCriteria, pageable);
			System.out.println(" tags " + tags);
			for (FindEmailVariableByIdOutput tag: tags) {
				map.put("{{" + tag.getPropertyName() + "}}",tag.getDefaultValue());
			}
		}
		catch(Exception ex ){
			map.put("tag1","tag one");
			map.put("{{tag2}}","tag two");
		}

		final String regex ="\\{\\{\\w+\\}\\}";// "\\{\\{\\w+}}"; //"{{\\w+}}";

		final Matcher m = Pattern.compile(regex).matcher(input);

		final List<String> matches = new ArrayList<>();
		while (m.find()) {
			matches.add(m.group(0));
			input = input.replaceAll(Pattern.quote(m.group(0)),map.get(m.group(0)));
		}
		return input;
	}

	public void sendEmail(CreateEmailTemplateInput email) throws IOException
	{
		if(email.getActive()!=null && email.getActive()) {
			String html = emailTemplateAppService.convertJsonToHtml(replaceVariable(email.getContentJson()));
			email.setContentHtml(html);
			if (email.getAttachmentpath() != null && !email.getAttachmentpath().isEmpty() )
				sendMessageWithAttachment(email.getTo(), email.getSubject(), email.getContentHtml(), email.getAttachmentpath());
			else
				sendSimpleMessage(email.getTo(), email.getSubject(), email.getContentHtml());
		}
	}
	
}