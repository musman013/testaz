package com.nfinity.ll.testaz.scheduler.application.dto;

public class JobListOutput {

	private String jobName;
	private String jobGroup;
	private String jobClass;
	private String jobDescription;
	private String jobStatus;

	public JobListOutput()
	{

	}

	public JobListOutput(String jobName, String jobGroup, String jobClass, String jobDescription, String jobStatus) {
		super();
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobClass = jobClass;
		this.jobDescription = jobDescription;
		this.jobStatus = jobStatus;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}


}
