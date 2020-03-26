package com.nfinity.ll.testaz.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "qrtzTriggers")
public class TriggerDetailsEntity implements Serializable {
	
	    private Long id; 
	    private String schedName;
	    private String jobName;
	    private String jobGroup;
	    private String description;
	    private String triggerName;
	    private String triggerGroup;
	    private Long nextFireTime;
	    private Long prevFireTime;
	    private String priority;
	    private String triggerState;
	    private String triggerType;
	    private Long startTime;
	    private Long endTime;
	    private String calendarName;	   
	    private String misfireInstr;
	    private byte[] jobData;
	    
	    
	    @Column(name = "Id", nullable = true)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }
		@Basic
		@Column(name = "schedName", nullable = false, length = 256)
		public String getSchedName() {
			return schedName;
		}
		public void setSchedName(String schedName) {
			this.schedName = schedName;
		}
		
		@Basic
		@Column(name = "jobName", nullable = false, length = 256)
		public String getJobName() {
			return jobName;
		}
		public void setJobName(String jobName) {
			this.jobName = jobName;
		}
		
		@Basic
		@Column(name = "jobGroup", nullable = false, length = 256)
		public String getJobGroup() {
			return jobGroup;
		}
		public void setJobGroup(String jobGroup) {
			this.jobGroup = jobGroup;
		}
		
		@Basic
		@Column(name = "description", nullable = true, length = 256)
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		@Id
		@Column(name = "triggerName", nullable = false, length = 256)
		public String getTriggerName() {
			return triggerName;
		}
		public void setTriggerName(String triggerName) {
			this.triggerName = triggerName;
		}
		
		@Basic
		@Column(name = "triggerGroup", nullable = false, length = 256)
		public String getTriggerGroup() {
			return triggerGroup;
		}
		public void setTriggerGroup(String triggerGroup) {
			this.triggerGroup = triggerGroup;
		}
		
		@Basic
		@Column(name = "nextFireTime", nullable = true, length = 256)
		public Long getNextFireTime() {
			return nextFireTime;
		}
		public void setNextFireTime(Long nextFireTime) {
			this.nextFireTime = nextFireTime;
		}
		
		@Basic
		@Column(name = "prevFireTime", nullable = true, length = 256)
		public Long getPrevFireTime() {
			return prevFireTime;
		}
		public void setPrevFireTime(Long prevFireTime) {
			this.prevFireTime = prevFireTime;
		}
		
		@Basic
		@Column(name = "priority", nullable = true, length = 256)
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
		
		@Basic
		@Column(name = "triggerState", nullable = false, length = 256)
		public String getTriggerState() {
			return triggerState;
		}
		public void setTriggerState(String triggerState) {
			this.triggerState = triggerState;
		}
		
		@Basic
		@Column(name = "triggerType", nullable = false, length = 256)
		public String getTriggerType() {
			return triggerType;
		}
		public void setTriggerType(String triggerType) {
			this.triggerType = triggerType;
		}
		
		@Basic
		@Column(name = "startTime", nullable = false, length = 256)
		public Long getStartTime() {
			return startTime;
		}
		public void setStartTime(Long startTime) {
			this.startTime = startTime;
		}
		
		@Basic
		@Column(name = "endTime", nullable = true, length = 256)
		public Long getEndTime() {
			return endTime;
		}
		public void setEndTime(Long endTime) {
			this.endTime = endTime;
		}
		
		@Basic
		@Column(name = "calendarName", nullable = true, length = 256)
		public String getCalendarName() {
			return calendarName;
		}
		public void setCalendarName(String calendarName) {
			this.calendarName = calendarName;
		}
		
		@Basic
		@Column(name = "misfireInstr", nullable = true, length = 256)
		public String getMisfireInstr() {
			return misfireInstr;
		}
		public void setMisfireInstr(String misfireInstr) {
			this.misfireInstr = misfireInstr;
		}
		
		@Basic
		@Column(name = "jobData", nullable = true, length = 256)
		public byte[] getJobData() {
			return jobData;
		}
		public void setJobData(byte[] jobData) {
			this.jobData = jobData;
		}






}
