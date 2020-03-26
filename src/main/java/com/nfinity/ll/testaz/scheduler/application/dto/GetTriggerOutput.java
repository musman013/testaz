package com.nfinity.ll.testaz.scheduler.application.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetTriggerOutput {


    private int repeatCount = 0;
    private int repeatInterval = 0;
    private boolean repeatIndefinite = false;
    private String cronExpression;
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String triggerName;
    private String triggerGroup;
    private String triggerState;
    private String triggerType;
    private String triggerDescription;
    private Date startTime;
    private Date endTime;
    private Date lastExecutionTime;
    private Date nextExecutionTime;
    private Map<String, String> jobMapData = new HashMap<String, String>();
    private Map<String, String> triggerMapData = new HashMap<String, String>();
   
	public String getJobName() {
        return jobName;
    }

    public boolean isRepeatIndefinite() {
        return repeatIndefinite;
    }

    public void setRepeatIndefinite(boolean repeatIndefinite) {
        this.repeatIndefinite = repeatIndefinite;
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


    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(Date lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public Map<String, String> getJobMapData() {
        return jobMapData;
    }

    public void setJobMapData(Map<String, String> jobMapData) {
        this.jobMapData = jobMapData;
    }

    public String getTriggerDescription() {
        return triggerDescription;
    }

    public void setTriggerDescription(String triggerDescription) {
        this.triggerDescription = triggerDescription;
    }

    public Map<String, String> getTriggerMapData() {
        return triggerMapData;
    }

    public void setTriggerMapData(Map<String, String> triggerMapData) {
        this.triggerMapData = triggerMapData;
    }

//	public boolean isDurable() {
//		return isDurable;
//	}
//	public void setDurable(boolean isDurable) {
//		this.isDurable = isDurable;
//	}
//	public String getJobStatus() {
//		return jobStatus;
//	}
//	public void setJobStatus(String jobStatus) {
//		this.jobStatus = jobStatus;
//	}

}
