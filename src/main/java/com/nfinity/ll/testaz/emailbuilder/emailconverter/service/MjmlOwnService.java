package com.nfinity.ll.testaz.emailbuilder.emailconverter.service;

import static com.nfinity.ll.testaz.emailbuilder.emailconverter.utils.CommonUtil.writeFile;
import static com.nfinity.ll.testaz.emailbuilder.emailconverter.utils.CommonUtil.deleteFile;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.request.Request;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.dto.response.Response;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.utils.EmailMjmlTemplateGenrator;
import com.nfinity.ll.testaz.emailbuilder.emailconverter.utils.MjmlCommandLine;

@Service
public class MjmlOwnService {

	@Autowired
  private EmailMjmlTemplateGenrator mjmlTemplateGenrator;
  //
  // private static final String DUMMY_TEMPLATE =
  // "<mjml><mj-body><mj-container><mj-section><mj-column><mj-text
  // \"message\"></mj-text></mj-column></mj-section></mj-container></mj-body></mjml>";

  @Value("${mjmlFile.path}")
  private String mjmlFilePath;

  public Response genrateHtml(Request request) throws IOException {
    String mjmlTemplate = mjmlTemplateGenrator.genrateTemplate(request);
    String file = String.format(mjmlFilePath, new Date().getTime());
    writeFile.accept(mjmlTemplate, file);
    String resultHtmlMail = MjmlCommandLine.executeCommand(file);
    deleteFile(file);
    return new Response(resultHtmlMail);
  }

  public Response genrateMjml(Request request) throws IOException {
    String mjmlTemplate = mjmlTemplateGenrator.genrateTemplate(request);
    return new Response("", mjmlTemplate);
  }
}
