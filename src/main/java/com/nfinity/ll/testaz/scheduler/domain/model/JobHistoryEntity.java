package com.nfinity.ll.testaz.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "JobHistory")
public class JobHistoryEntity implements Serializable {

    private Long id;
    private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String triggerName;
    private String triggerGroup;
    private String jobClass;
    private Date firedTime;
    private Date finishedTime;
    private String duration;
    private String jobStatus;
    private String jobMapData;


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
    @Column(name = "JobName", nullable = false, length = 256)
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Basic
    @Column(name = "JobDescription", nullable = true, length = 1024)
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Basic
    @Column(name = "JobGroup", nullable = false, length = 256)
    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Basic
    @Column(name = "JobClass", nullable = false, length = 256)
    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    @Basic
    @Column(name = "FiredTime", nullable = false)
    public Date getFiredTime() {
        return firedTime;
    }

    public void setFiredTime(Date firedTime) {
        this.firedTime = firedTime;
    }

    @Basic
    @Column(name = "FinishedTime", nullable = true)
    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    @Basic
    @Column(name = "TriggerName", nullable = false, length = 256)
    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    @Basic
    @Column(name = "TriggerGroup", nullable = false, length = 256)
    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    @Basic
    @Column(name = "Duration", nullable = false, length = 256)
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "JobStatus", nullable = false, length = 256)
    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    @Basic
    @Column(name = "JobMapData", nullable = true, length = 256)
    public String getJobMapData() {
        return jobMapData;
    }

    public void setJobMapData(String jobMapData) {
        this.jobMapData = jobMapData;
    }

    @Override
    public boolean equals(Object o) {
    	 if (this == o) return true;
    	 if (!(o instanceof JobHistoryEntity)) return false;
         JobHistoryEntity that = (JobHistoryEntity) o;
         return id != null && id.equals(that.id);
       
    }

    @Override
    public int hashCode() {
    	 return 31;
    	 
    }
}