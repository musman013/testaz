package com.nfinity.ll.testaz.scheduler.application.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobDetails {

    private String jobName;
    private String jobGroup;
    private String jobClass;
    private String jobDescription;
    private boolean isDurable ;
    private Map<String, String> jobMapData = new HashMap<String, String>();
    private List<TriggerDetails> triggerDetails = new ArrayList<TriggerDetails>();
    private String jobStatus;
 
    public JobDetails() {

    }

	public JobDetails(String jName, String jGroup, String jClass, String jobDescription, Map<String, String> jMapData, List<TriggerDetails> triggerDetails, boolean isDurable, String jobStatus) {
        super();
        this.jobName = jName;
        this.jobGroup = jGroup;
        this.jobClass = jClass;
        this.jobDescription = jobDescription;
        this.jobMapData = jMapData;
        this.isDurable = isDurable;
        this.jobStatus = jobStatus;
        this.setTriggerDetails(triggerDetails);
    }


    public List<TriggerDetails> getTriggerDetails() {
        return triggerDetails;
    }

    public void setTriggerDetails(List<TriggerDetails> triggerDetails) {
        this.triggerDetails = triggerDetails;
    }

  
	public void setJobName(String jName) {
        this.jobName = jName;
    }

    public void setJobGroup(String jGroup) {
        this.jobGroup = jGroup;
    }

    public void setJobClass(String jClass) {
        this.jobClass = jClass;
    }

    public void setJobStatus(String jStatus) {
        this.jobStatus = jStatus;
    }

    public Map<String, String> getJobMapData() {
        return jobMapData;
    }

    public void setJobMapData(Map<String, String> jobMapData) {
        this.jobMapData = jobMapData;
    }

    public boolean getIsDurable() {
		return isDurable;
	}

	public void setIsDurable(boolean isDurable) {
		this.isDurable = isDurable;
	}


    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getJobClass() {
        return jobClass;
    }

    public String getJobStatus() {
        return jobStatus;
    }


}