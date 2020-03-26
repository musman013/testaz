package com.nfinity.ll.testaz.emailbuilder.domain.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "Email")
public class EmailTemplateEntity implements Serializable {

    private Long id;
    private String templateName;
    private String category;
    private String contentHtml;
    private String contentJson;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
	private Boolean active;
	private String attachmentpath;
    
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Basic
	@Column(name = "TemplateName", nullable = false ,length = 256)
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	@Basic
	@Column(name = "Category", nullable = true ,length = 256)
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Basic
	@Column(name = "ContentHtml", nullable = true, length = 32768)
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	
	@Basic
	@Column(name = "ContentJson", nullable = true, length = 32768)	
	public String getContentJson() {
		return contentJson;
	}
	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}
	
	@Basic
	@Column(name = "To_", nullable = false,length = 256)	
	@Email
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	@Basic
	@Column(name = "Cc", nullable = true ,length = 256)
	@Email
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}

	@Basic
	@Column(name = "Bcc", nullable = true ,length = 256)
	@Email
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	@Basic
	@Column(name = "Subject", nullable = true ,length = 256)
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Basic
	@Column(name = "active", nullable = true)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active){
		this.active = active;
	}

	@Basic
	@Column(name = "attachmentpath", nullable = true, length =256)
	public String getAttachmentpath() {
		return attachmentpath;
	}

	public void setAttachmentpath(String attachmentpath){
		this.attachmentpath = attachmentpath;
	}
}
