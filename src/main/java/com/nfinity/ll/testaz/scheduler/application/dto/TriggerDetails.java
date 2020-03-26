package com.nfinity.ll.testaz.scheduler.application.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TriggerDetails {


    private String triggerName;
    private String triggerGroup;
    private String triggerState;
    private String triggerType;
    private String triggerDescription;
    private Map<String, String> triggerMapData = new HashMap<String, String>();
    private Date startTime;
    private Date endTime;
    private Date lastExecutionTime;
    private Date nextExecutionTime;

    public TriggerDetails() {

    }

    public TriggerDetails(String triggerName, String triggerGroup, String triggerState, String triggerType, String triggerDescription,
                          Map<String, String> triggerMapData, Date startTime, Date endTime,
                          Date lastExecutionTime, Date nextExecutionTime) {
        super();
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.triggerState = triggerState;
        this.triggerType = triggerType;
        this.triggerDescription = triggerDescription;
        this.triggerMapData = triggerMapData;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastExecutionTime = lastExecutionTime;
        this.nextExecutionTime = nextExecutionTime;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
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


}
