package com.nfinity.ll.testaz.scheduler.application.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetJobOutput {

    private long id;
    private String jobName;
    private String jobDescription;
    private String jobGroup;
    private String jobClass;
    private String triggerName;
    private String triggerGroup;
    private Date firedTime;
    private Date finishedTime;
    private String duration;
    private String jobStatus;
    private Map<String, String> jobMapData = new HashMap<String, String>();


    public GetJobOutput() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Date getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(Date firedTime) {
        this.firedTime = firedTime;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Map<String, String> getJobMapData() {
        return jobMapData;
    }

    public void setJobMapData(Map<String, String> jobMapData) {
        this.jobMapData = jobMapData;
    }


}
