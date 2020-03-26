package com.nfinity.ll.testaz.scheduler.application.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TriggerCreationDetails {

    private int repeatCount;
    private int repeatInterval;
    private String triggerName;
    private String triggerGroup;
    private String triggerType;
    private Date startTime;
    private Date endTime;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private String cronExpression;
    private String triggerDescription;
    private Map<String, String> triggerMapData = new HashMap<String, String>();
    private boolean repeatIndefinite;


    public TriggerCreationDetails() {

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

    public boolean getRepeatIndefinite() {
        return repeatIndefinite;
    }

    public void setRepeatIndefinite(boolean repeatIndefinite) {
        this.repeatIndefinite = repeatIndefinite;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
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

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

}
