package com.nfinity.ll.testaz.scheduler.domain.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "qrtzJobDetails")
public class JobDetailsEntity implements Serializable {

	    private Long id; 
	    private String jobName;
	    private String jobGroup;
	    private String description;
	    private String schedName;
	    private Boolean isUpdateData;
	    private String jobClassName;
	    private Boolean isDurable;
	    private Boolean isNonconcurrent;
	    private byte[] jobData;
	    private Boolean requestsRecovery;
	    

	    @Column(name = "Id", nullable = true)
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }
	    @Id
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
		
		@Basic
		@Column(name = "schedName", nullable = false, length = 256)
		public String getSchedName() {
			return schedName;
		}
		public void setSchedName(String schedName) {
			this.schedName = schedName;
		}
		
		@Basic
		@Column(name = "isUpdateData", nullable = false, length = 256)
		public Boolean getIsUpdateData() {
			return isUpdateData;
		}
		public void setIsUpdateData(Boolean isUpdateData) {
			this.isUpdateData = isUpdateData;
		}
		
		@Basic
		@Column(name = "jobClassName", nullable = false, length = 256)
		public String getJobClassName() {
			return jobClassName;
		}
		public void setJobClassName(String jobClassName) {
			this.jobClassName = jobClassName;
		}
		
		@Basic
		@Column(name = "isDurable", nullable = false, length = 256)
		public Boolean getIsDurable() {
			return isDurable;
		}
		public void setIsDurable(Boolean isDurable) {
			this.isDurable = isDurable;
		}
		
		@Basic
		@Column(name = "isNonconcurrent", nullable = false, length = 256)
		public Boolean getIsNonconcurrent() {
			return isNonconcurrent;
		}
		public void setIsNonconcurrent(Boolean isNonconcurrent) {
			this.isNonconcurrent = isNonconcurrent;
		}
		
		@Basic
		@Column(name = "jobData", nullable = true, length = 256)
		public byte[] getJobData() {
			return jobData;
		}
		public void setJobData(byte[] jobData) {
			this.jobData = jobData;
		}
		
		@Basic
		@Column(name = "requestsRecovery", nullable = false, length = 256)
		public Boolean getRequestsRecovery() {
			return requestsRecovery;
		}
		public void setRequestsRecovery(Boolean isRequestsRecovery) {
			this.requestsRecovery = isRequestsRecovery;
		}
	    
	    
	    
	    
	
}

