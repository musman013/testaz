package com.nfinity.ll.testaz.scheduler.schedulerservice;

import com.nfinity.ll.testaz.scheduler.schedulerservice.CGenClassLoader;
import com.nfinity.ll.testaz.scheduler.constants.QuartzConstants;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchFields;
import com.nfinity.ll.testaz.scheduler.application.dto.*;
import com.nfinity.ll.testaz.scheduler.domain.model.QJobDetailsEntity;
import com.nfinity.ll.testaz.scheduler.domain.model.QJobHistoryEntity;
import com.nfinity.ll.testaz.scheduler.domain.model.QTriggerDetailsEntity;
import com.nfinity.ll.testaz.scheduler.domain.*;
import com.nfinity.ll.testaz.scheduler.domain.model.*;
import com.nfinity.ll.testaz.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import org.quartz.*;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Validated
public class SchedulerService {
	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private Environment env;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private SchedulerServiceUtil schedulerServiceUtil;

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	TriggerDetailsManager triggerDetailsManager;

	@Autowired
	JobHistoryManager jobHistoryManager;

	@Autowired
	JobDetailsManager jobDetailsManager;

	private Scheduler scheduler;

	private QuartzConstants quartzConstant;


	public Scheduler getScheduler() throws SchedulerException
	{
		scheduler=schedulerFactoryBean.getScheduler();
		//scheduler.start();
		return scheduler;
	}

	//List all jobs
	public List<JobListOutput> listAllJobs(SearchCriteria search,Pageable pageable) throws Exception {

		List<JobListOutput> list = new ArrayList<JobListOutput>();
		//Get Jobs 
		Page<JobDetailsEntity> foundJobs = jobDetailsManager.findAll(searchJobDetails(search),pageable);
		List<JobDetailsEntity> jobList = foundJobs.getContent();

		for (int i = 0; i < jobList.size(); i++) {
			JobListOutput obj = new JobListOutput();

			obj.setJobName(jobList.get(i).getJobName());
			obj.setJobGroup(jobList.get(i).getJobGroup());
			obj.setJobClass(jobList.get(i).getJobClassName());
			obj.setJobDescription(jobList.get(i).getDescription());

			if (schedulerServiceUtil.isJobRunning(jobList.get(i).getJobName(), jobList.get(i).getJobGroup()))
				obj.setJobStatus(quartzConstant.JOB_STATUS_RUNNING);
			else
				obj.setJobStatus(schedulerServiceUtil.getJobState(jobList.get(i).getJobName(),jobList.get(i).getJobGroup()));

			list.add(obj);
		}
		return list;
	}

	public BooleanBuilder searchJobDetails(SearchCriteria search) throws Exception {

		QJobDetailsEntity job = QJobDetailsEntity.jobDetailsEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllPropertiesForJobDetails(job, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkPropertiesForJobDetails(keysList);
				return searchSpecificPropertyForJobDetails(job,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkPropertiesForJobDetails(keysList);
				return searchKeyValuePairForJobDetails(job, map);
			}
		}
		return null;
	}

	public BooleanBuilder searchAllPropertiesForJobDetails(QJobDetailsEntity jobDetails,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(jobDetails.jobName.likeIgnoreCase("%"+ value + "%"));
			builder.or(jobDetails.jobGroup.likeIgnoreCase("%"+ value + "%"));
			builder.or(jobDetails.jobClassName.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals")) {
			builder.or(jobDetails.jobName.eq(value));
			builder.or(jobDetails.jobGroup.eq(value));
			builder.or(jobDetails.jobClassName.eq(value));
		}

		return builder;
	}

	public void checkPropertiesForJobDetails(List<String> list) throws Exception
	{
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("jobName"))
					|| (list.get(i).replace("%20","").trim().equals("jobGroup"))
					|| (list.get(i).replace("%20","").trim().equals("jobClassName"))
					)) {

				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchSpecificPropertyForJobDetails(QJobDetailsEntity jobDetails,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("jobName")) {
				if(operator.equals("contains")) {
					builder.or(jobDetails.jobName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobDetails.jobName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("jobGroup")) {
				if(operator.equals("contains")) {
					builder.or(jobDetails.jobGroup.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobDetails.jobGroup.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("jobClassName")) {
				if(operator.equals("contains")) {
					builder.or(jobDetails.jobClassName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobDetails.jobClassName.eq(value));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePairForJobDetails(QJobDetailsEntity jobDetails, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("jobName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobDetails.jobName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobDetails.jobName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobDetails.jobName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("jobGroup")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobDetails.jobGroup.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobDetails.jobGroup.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobDetails.jobGroup.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("jobClassName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobDetails.jobClassName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobDetails.jobClassName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobDetails.jobClassName.ne(details.getValue().getSearchValue()));
				}
			}

		}
		return builder;
	}

	//List Triggers Against Job Key
	public List<TriggerDetails> returnTriggersForAJob(JobKey jobKey) throws SchedulerException {
		List<TriggerDetails> triggerDetails = new ArrayList<TriggerDetails>();

		if(getScheduler().checkExists(jobKey))
		{
			List<Trigger> triggers = (List<Trigger>) getScheduler().getTriggersOfJob(jobKey);
			for (int i = 0; i < triggers.size(); i++) {               
				triggerDetails.add(schedulerServiceUtil.returnTriggerDetails(triggers.get(i)));
			}
		}
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+jobKey.getName() +" and jobGroup=" +jobKey.getGroup());
			return null;
		}

		return triggerDetails;
	}

	public List<GetExecutingJob> currentlyExecutingJobs() throws SchedulerException {
		List<GetExecutingJob> jobsList = new ArrayList<GetExecutingJob>();
		List<JobExecutionContext> executingJobs = getScheduler().getCurrentlyExecutingJobs();

		for (int i = 0; i < executingJobs.size(); i++) {
			GetExecutingJob job = new GetExecutingJob();
			JobDetails jobDetail = schedulerServiceUtil.returnJobDetails(executingJobs.get(i).getJobDetail().getKey());
			job.setJobName(jobDetail.getJobName());
			job.setJobGroup(jobDetail.getJobGroup());
			job.setJobDescription(jobDetail.getJobDescription());
			job.setJobClass(jobDetail.getJobClass());
			job.setJobMapData(jobDetail.getJobMapData());
			job.setTriggerName(executingJobs.get(i).getTrigger().getKey().getName());
			job.setTriggerGroup(executingJobs.get(i).getTrigger().getKey().getGroup());
			job.setFiredTime(executingJobs.get(i).getFireTime());
			job.setNextExecutionTime(executingJobs.get(i).getNextFireTime());
			
			if(!jobsList.contains(job))
			{
				jobsList.add(job);
			}
		}

		return jobsList;
	}

	//List All Job Classes
	public List<String> listAllJobClasses() {

		String path =System.getProperty("user.dir");
		System.out.println(" path " + System.getProperty("user.dir"));
		String packageName=env.getProperty("fastCode.jobs.default");
		System.out.println(" package  " + packageName);
		CGenClassLoader loader = new CGenClassLoader(path.replace('\\', '/') +"/target/classes");
		ArrayList<Class<?>> jobClasses=null;
		try {
			jobClasses = loader.findClasses(packageName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> classNameList = new ArrayList<String>();

		for(Class<?> classObj : jobClasses)
		{
			classNameList.add(classObj.getName());
		}
		
		return classNameList;
	}

	//List All Job Groups
	public List<String> listAllJobGroups() throws SchedulerException, IOException {

		List<String> groupsList = getScheduler().getJobGroupNames();
		return groupsList;
	}

	// Pause a job
	public boolean pauseJob(String jobName, String jobGroup) throws SchedulerException {
		JobKey jobKey = new JobKey(jobName, jobGroup);

		if (getScheduler().checkExists(jobKey)) {
			getScheduler().pauseJob(jobKey);
			return true;
		}
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+jobName +" and jobGroup=" +jobGroup);
			return false;
		}
	}
	//Resume a job
	public boolean resumeJob(String jobName, String jobGroup) throws SchedulerException {
		JobKey jobKey = new JobKey(jobName, jobGroup);
		if (getScheduler().checkExists(jobKey)) {
			getScheduler().resumeJob(jobKey);
			return true;
		}
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+jobName +" and jobGroup=" +jobGroup);
			return false;
		}
	}

	//Delete a job
	public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
		JobKey jobKey = new JobKey(jobName, jobGroup);
		if (getScheduler().checkExists(jobKey)) {
			getScheduler().deleteJob(jobKey);
			return true;
		} 
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+jobName +" and jobGroup=" +jobGroup);
			return false;
		}
	}

	//Return a job
	public JobDetails returnJob(String jobName, String jobGroup) throws SchedulerException {

		JobKey jobKey = new JobKey(jobName, jobGroup);

		if (getScheduler().checkExists(jobKey)) {

			JobDetails jobDetails =schedulerServiceUtil.returnJobDetails(jobKey);
			List<Trigger> triggers = (List<Trigger>) getScheduler().getTriggersOfJob(jobKey);
			List<TriggerDetails> triggerDetails = new ArrayList<TriggerDetails>();
			for (int i = 0; i < triggers.size(); i++) {
				System.out.println("sizee ");
				triggerDetails.add(schedulerServiceUtil.returnTriggerDetails(triggers.get(i)));
			}
			jobDetails.setTriggerDetails(triggerDetails);
			return  jobDetails;	
		}
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+jobName +" and jobGroup=" +jobGroup);
			return null;	
		}

	}

	//Update a job
	public boolean updateJob(JobDetails obj) throws SchedulerException {

		JobKey jobKey = new JobKey(obj.getJobName(), obj.getJobGroup());
		if (getScheduler().checkExists(jobKey)) {
			JobDetail jobDetails = getScheduler().getJobDetail(jobKey).getJobBuilder()
					.storeDurably(obj.getIsDurable())
					.withDescription(obj.getJobDescription())
					.build();

			getScheduler().addJob(setJobMapData(jobDetails,obj.getJobMapData()), true, true);
			return true;

		} else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+obj.getJobName() +" and jobGroup=" +obj.getJobGroup());
			return false;
		}

	}

	//Update a Trigger
	public boolean updateTrigger(TriggerCreationDetails obj) throws SchedulerException {

		TriggerKey triggerKey =new TriggerKey(obj.getTriggerName(), obj.getTriggerGroup());
		if (getScheduler().checkExists(triggerKey)) {
			Trigger trigger;
			TriggerBuilder triggerBuilder;
			if(obj.getStartTime()==null) {
				obj.setStartTime(new Date());
			}
			if (obj.getEndTime() != null) {
				trigger = getScheduler().getTrigger(triggerKey);
				triggerBuilder = trigger.getTriggerBuilder();
				trigger = triggerBuilder.startAt(obj.getStartTime())
						.endAt(obj.getEndTime()).withDescription(obj.getTriggerDescription())
						.build();
				getScheduler().rescheduleJob(triggerKey,setJobMapDataInTrigger(trigger, obj.getTriggerMapData()));
			} 
			if(obj.getEndTime() ==null) {
				trigger = getScheduler().getTrigger(triggerKey);
				triggerBuilder = trigger.getTriggerBuilder();
				trigger = triggerBuilder.startAt(obj.getStartTime())
						.withDescription(obj.getTriggerDescription()).build();

				getScheduler().rescheduleJob(triggerKey,setJobMapDataInTrigger(trigger, obj.getTriggerMapData()));
			}
			if (obj.getTriggerType().equalsIgnoreCase("Simple")) { 
				return schedulerServiceUtil.updateSimpleTrigger(obj, triggerKey);
			}
			else if (obj.getTriggerType().equalsIgnoreCase("Cron")) {
				return schedulerServiceUtil.updateCronTrigger(obj, triggerKey);
			}
			else {
				logHelper.getLogger().error("Trigger type not found");
				return false;
			}
		} 
		else {
			logHelper.getLogger().error("There does not exist a trigger with a triggerName="+obj.getTriggerName() +" and triggerGroup=" +obj.getTriggerGroup());
			return false;	
		}
	}

	// Create a job
	public boolean createJob(JobDetails obj) throws SchedulerException, ClassNotFoundException{

		if (!(getScheduler().checkExists(new JobKey(obj.getJobName(), obj.getJobGroup())))) {
			Class<? extends Job> className = (Class<? extends Job>) Class.forName(obj.getJobClass());
			JobDetail jobDetails = JobBuilder.newJob(className)
					.storeDurably(obj.getIsDurable())
					.withDescription(obj.getJobDescription())
					.withIdentity(obj.getJobName(), obj.getJobGroup()).build();
			getScheduler().addJob(setJobMapData(jobDetails,obj.getJobMapData()), true, true);
			return true;
		} 
		else {
			logHelper.getLogger().error("There already exists a job with a jobName="+ obj.getJobName() ," and jobGroup=" +obj.getJobGroup());
			return false;
		}
	}

	public JobDetail setJobMapData(JobDetail jobDetails,Map<String,String> mapData)
	{
		if(mapData !=null) {
			Set set = mapData.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry) iterator.next();                            
				jobDetails.getJobDataMap().put(mentry.getKey().toString(), mentry.getValue().toString());
			}
		}
		return jobDetails;
	}

	//Cancel a Trigger
	public boolean cancelTrigger(String triggerName, String triggerGroup) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

		if (getScheduler().checkExists(triggerKey)) {             
			getScheduler().unscheduleJob(triggerKey);
			return true ;
		}
		else {
			logHelper.getLogger().error("There does not exist a trigger with a triggerName="+ triggerName +" and triggerGroup=" + triggerGroup);
			return false;
		}
	}

	//Return Trigger Details
	public GetTriggerOutput returnTrigger(String triggerName, String triggerGroup) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

		if (getScheduler().checkExists(triggerKey)) {
			GetTriggerOutput obj = new GetTriggerOutput();
			Trigger trigger = getScheduler().getTrigger(triggerKey);
			JobKey jobKey = trigger.getJobKey();

			String trigType = quartzConstant.SIMPLE_TRIGGER;
			if (trigger.getClass().toString().equals(quartzConstant.CRON_TRIGGER_CLASS))
				trigType = quartzConstant.CRON_TRIGGER;

			String[] jobKeyValues = getScheduler().getJobDetail(jobKey).getJobDataMap().getKeys();
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < jobKeyValues.length; i++) {
				String str = getScheduler().getJobDetail(jobKey).getJobDataMap().getString(jobKeyValues[i]);
				map.put(jobKeyValues[i], str);
			}

			String[] triggerKeyValues = getScheduler().getTrigger(triggerKey).getJobDataMap().getKeys();
			Map<String, String> triggerMap = new HashMap<String, String>();
			for (int i = 0; i < triggerKeyValues.length; i++) {
				String str = getScheduler().getTrigger(triggerKey).getJobDataMap().getString(triggerKeyValues[i]);
				triggerMap.put(triggerKeyValues[i], str);
			}

			obj.setJobName(jobKey.getName());
			obj.setJobGroup(jobKey.getGroup());
			obj.setJobDescription(getScheduler().getJobDetail(jobKey).getDescription());
			obj.setTriggerName(triggerKey.getName());
			obj.setTriggerGroup(triggerKey.getGroup());
			obj.setJobMapData(map);
			obj.setStartTime(trigger.getStartTime());
			obj.setLastExecutionTime(trigger.getPreviousFireTime());
			obj.setNextExecutionTime(trigger.getNextFireTime());
			obj.setEndTime(trigger.getEndTime());
			obj.setTriggerState(getScheduler().getTriggerState(triggerKey).toString());
			obj.setTriggerDescription(getScheduler().getTrigger(triggerKey).getDescription());
			obj.setTriggerMapData(triggerMap);
			obj.setTriggerType(trigType);

			if (trigType.equalsIgnoreCase(quartzConstant.CRON_TRIGGER)) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				obj.setCronExpression(cronTrigger.getCronExpression());
			} else if (trigType.equalsIgnoreCase(quartzConstant.SIMPLE_TRIGGER)) {
				SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
				obj.setRepeatCount(simpleTrigger.getRepeatCount());
				int seconds = (int) simpleTrigger.getRepeatInterval() / 1000;
				obj.setRepeatInterval(seconds);
				if (simpleTrigger.REPEAT_INDEFINITELY == -1) {
					obj.setRepeatIndefinite(false);
				} else
					obj.setRepeatIndefinite(true);

			}   
			return obj;
		}
		else {
			logHelper.getLogger().error("There does not exist a trigger with a triggerName="+ triggerName +" and triggerGroup=" + triggerGroup);
			return null;
		}
	}

	//Pause a Trigger
	public boolean pauseTrigger(String triggerName, String triggerGroup) throws SchedulerException {

		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
		if (getScheduler().checkExists(triggerKey)) {
			getScheduler().pauseTrigger(triggerKey);
			return true;
		}
		else {
			logHelper.getLogger().error("There does not exist a trigger with a triggerName="+ triggerName +" and triggerGroup=" + triggerGroup);
			return false;
		}         
	}

	//Resume a trigger
	public boolean resumeTrigger(String triggerName, String triggerGroup) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);

		if (getScheduler().checkExists(triggerKey)) {
			getScheduler().resumeTrigger(triggerKey);
			return true;
		}
		else {
			logHelper.getLogger().error("There does not exist a trigger with a triggerName="+ triggerName +" and triggerGroup=" + triggerGroup);
			return false;
		}
	}
	//Create a Trigger
	public boolean createTrigger(TriggerCreationDetails obj) throws SchedulerException {

		if (getScheduler().checkExists(new JobKey(obj.getJobName(), obj.getJobGroup()))) {
			JobDetail jobDetail = getScheduler().getJobDetail(new JobKey(obj.getJobName(), obj.getJobGroup()));

			if (!(getScheduler().checkExists(new TriggerKey(obj.getTriggerName(), obj.getTriggerGroup())))) {
				if(obj.getTriggerType().equalsIgnoreCase("Simple")) {
					Trigger trigger =schedulerServiceUtil.createSimpleTrigger(obj, jobDetail);
					getScheduler().scheduleJob(setJobMapDataInTrigger(trigger,obj.getTriggerMapData()));
					return true;
				}
				else if(obj.getTriggerType().equalsIgnoreCase("Cron")) {
					Trigger trigger=schedulerServiceUtil.createCronTrigger(obj, jobDetail);
					getScheduler().scheduleJob(setJobMapDataInTrigger(trigger,obj.getTriggerMapData()));
					return true;
				}
				else {
					logHelper.getLogger().error("Trigger type not found");
					return false;
				}
			}
			else {
				logHelper.getLogger().error("There already exists a trigger with a triggerName="+ obj.getTriggerName() +" and triggerGroup=" + obj.getTriggerGroup());
				return false;
			}
		}
		else {
			logHelper.getLogger().error("There does not exist a job with a jobName="+ obj.getJobName() +" and jobGroup=" + obj.getJobGroup());
			return false;
		}
	}

	public Trigger setJobMapDataInTrigger(Trigger trigger, Map<String,String> mapData)
	{ 
		if (mapData !=null) {
			Set set = mapData.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry) iterator.next();                           
				trigger.getJobDataMap().put(mentry.getKey().toString(), mentry.getValue().toString());
			}
		}
		return trigger;
	}

	//List Trigger Groups 
	public List<String> listAllTriggerGroups() throws SchedulerException {
		List<String> list = new ArrayList<String>();

		list = getScheduler().getTriggerGroupNames();
		return list;
	}

	//List All Triggers
	public List<GetTriggerOutput> listAllTriggers(SearchCriteria search,Pageable pageable) throws Exception {
		List<GetTriggerOutput> list = new ArrayList<GetTriggerOutput>();

		Page<TriggerDetailsEntity> foundTriggers = triggerDetailsManager.findAll(searchTriggerDetails(search),pageable);
		List<TriggerDetailsEntity> triggerList = foundTriggers.getContent();

		for (int i = 0; i < triggerList.size(); i++) {
			GetTriggerOutput obj = new GetTriggerOutput();

			obj.setJobName(triggerList.get(i).getJobName());
			obj.setJobGroup(triggerList.get(i).getJobGroup());
			obj.setTriggerName(triggerList.get(i).getTriggerName());
			obj.setTriggerGroup(triggerList.get(i).getTriggerGroup());
			obj.setTriggerState(triggerList.get(i).getTriggerState());
			obj.setTriggerType(triggerList.get(i).getTriggerType());
			obj.setTriggerDescription(triggerList.get(i).getDescription());
			obj.setStartTime(millisToDate(triggerList.get(i).getStartTime()));
			obj.setEndTime(millisToDate(triggerList.get(i).getEndTime()));
			obj.setLastExecutionTime(millisToDate(triggerList.get(i).getPrevFireTime()));
			obj.setNextExecutionTime(millisToDate(triggerList.get(i).getNextFireTime()));

			list.add(obj);
		}

		return list;
	}

	public BooleanBuilder searchTriggerDetails(SearchCriteria search) throws Exception {

		QTriggerDetailsEntity triggerDetails = QTriggerDetailsEntity.triggerDetailsEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllPropertiesForTriggerDetails(triggerDetails, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkPropertiesForTriggerDetails(keysList);
				return searchSpecificPropertyForTriggerDetails(triggerDetails,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkPropertiesForTriggerDetails(keysList);
				return searchKeyValuePairForTriggerDetails(triggerDetails, map);
			}
		}
		return null;
	}

	public BooleanBuilder searchAllPropertiesForTriggerDetails(QTriggerDetailsEntity triggerDetails,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(triggerDetails.jobName.likeIgnoreCase("%"+ value + "%"));
			builder.or(triggerDetails.jobGroup.likeIgnoreCase("%"+ value + "%"));
			builder.or(triggerDetails.triggerGroup.likeIgnoreCase("%"+ value + "%"));
			builder.or(triggerDetails.triggerName.likeIgnoreCase("%"+ value + "%"));
			builder.or(triggerDetails.triggerState.likeIgnoreCase("%"+ value + "%"));
			builder.or(triggerDetails.triggerType.likeIgnoreCase("%"+ value + "%"));

		}
		else if(operator.equals("equals")) {
			builder.or(triggerDetails.jobName.eq(value));
			builder.or(triggerDetails.jobGroup.eq(value));
			builder.or(triggerDetails.triggerGroup.eq(value));
			builder.or(triggerDetails.triggerName.eq(value));
			builder.or(triggerDetails.triggerState.eq(value));
			builder.or(triggerDetails.triggerType.eq(value));

			Long date=dateStringToMillis(value);

			if(date !=null) {
				builder.or(triggerDetails.endTime.eq(date));
				builder.or(triggerDetails.nextFireTime.eq(date));
				builder.or(triggerDetails.startTime.eq(date));
				builder.or(triggerDetails.prevFireTime.eq(date));
			}
		}

		return builder;
	}

	public void checkPropertiesForTriggerDetails(List<String> list) throws Exception
	{
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("jobName"))
					|| (list.get(i).replace("%20","").trim().equals("jobGroup"))
					|| (list.get(i).replace("%20","").trim().equals("triggerName"))
					|| (list.get(i).replace("%20","").trim().equals("triggerGroup"))
					|| (list.get(i).replace("%20","").trim().equals("triggerState"))
					|| (list.get(i).replace("%20","").trim().equals("triggerType"))
					|| (list.get(i).replace("%20","").trim().equals("nextFireTime"))
					|| (list.get(i).replace("%20","").trim().equals("prevFireTime"))
					|| (list.get(i).replace("%20","").trim().equals("endTime"))
					|| (list.get(i).replace("%20","").trim().equals("startTime"))))
			{
				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchSpecificPropertyForTriggerDetails(QTriggerDetailsEntity triggerDetails,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("jobName")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.jobName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.jobName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("jobGroup")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.jobGroup.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.jobGroup.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerName")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.triggerName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.triggerName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerGroup")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.triggerGroup.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.triggerGroup.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerState")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.triggerState.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.triggerState.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerType")) {
				if(operator.equals("contains")) {
					builder.or(triggerDetails.triggerType.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(triggerDetails.triggerType.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("nextFireTime")) {
				Long date=dateStringToMillis(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(triggerDetails.nextFireTime.eq(date));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("prevFireTime")) {
				Long date=dateStringToMillis(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(triggerDetails.prevFireTime.eq(date));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("startTime")) {
				Long date=dateStringToMillis(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(triggerDetails.startTime.eq(date));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("endTime")) {
				Long date=dateStringToMillis(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(triggerDetails.endTime.eq(date));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePairForTriggerDetails(QTriggerDetailsEntity triggerDetails, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {

			if(details.getKey().replace("%20","").trim().equals("jobName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.jobName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.jobName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.jobName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("jobGroup")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.jobGroup.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.jobGroup.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.jobGroup.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("triggerName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.triggerName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.triggerName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.triggerName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("triggerGroup")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.triggerGroup.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.triggerGroup.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.triggerGroup.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("triggerState")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.triggerState.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.triggerState.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.triggerState.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("triggerType")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(triggerDetails.triggerType.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(triggerDetails.triggerType.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(triggerDetails.triggerType.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("nextFireTime")) {
				Long date= dateStringToMillis(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(triggerDetails.nextFireTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(triggerDetails.nextFireTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Long startDate= dateStringToMillis(details.getValue().getStartingValue());
					Long endDate= dateStringToMillis(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(triggerDetails.nextFireTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(triggerDetails.nextFireTime.loe(endDate));
					else if(startDate!=null)
						builder.and(triggerDetails.nextFireTime.goe(startDate));  
				}

			}
			if(details.getKey().replace("%20","").trim().equals("prevFireTime")) {
				Long date= dateStringToMillis(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(triggerDetails.prevFireTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(triggerDetails.prevFireTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Long startDate= dateStringToMillis(details.getValue().getStartingValue());
					Long endDate= dateStringToMillis(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(triggerDetails.prevFireTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(triggerDetails.prevFireTime.loe(endDate));
					else if(startDate!=null)
						builder.and(triggerDetails.prevFireTime.goe(startDate));  
				}
			}
			if(details.getKey().replace("%20","").trim().equals("startTime")) {
				Long date= dateStringToMillis(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(triggerDetails.startTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(triggerDetails.startTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Long startDate= dateStringToMillis(details.getValue().getStartingValue());
					Long endDate= dateStringToMillis(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(triggerDetails.startTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(triggerDetails.startTime.loe(endDate));
					else if(startDate!=null)
						builder.and(triggerDetails.startTime.goe(startDate));  
				}
			}
			if(details.getKey().replace("%20","").trim().equals("endTime")) {
				Long date= dateStringToMillis(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(triggerDetails.endTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(triggerDetails.endTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Long startDate= dateStringToMillis(details.getValue().getStartingValue());
					Long endDate= dateStringToMillis(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(triggerDetails.endTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(triggerDetails.endTime.loe(endDate));
					else if(startDate!=null)
						builder.and(triggerDetails.endTime.goe(startDate));  
				}
			}
		}

		return builder;
	}

	public Long dateStringToMillis(String str)
	{
		if(str !=null)
		{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date;
		try {
			date = formatter.parse(str);
			long mills = date.getTime();
			return mills;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		}
		else
			return null;
	}

	public Date stringToDate(String str)
	{
		if(str !=null)
		{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date;
		try {
			date = formatter.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		}
		else
			return null;
	}

	public Date millisToDate(long durationInMillis) throws ParseException {
		if(durationInMillis > 0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			//SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
			//formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			String formattedDate = formatter.format(new Date(durationInMillis));
			Date date = formatter.parse(formattedDate);
			return date;
		} 
		else
			return null; 
	}

	//Execution History By Job
	public List<GetJobOutput> executionHistoryByJob(String jobName, String jobGroup) {

		List<JobHistoryEntity> jobList = jobHistoryManager.findByJob(jobName, jobGroup);
		return schedulerServiceUtil.returnJobOutput(jobList);
	}

	//Execution History By Trigger
	public List<GetJobOutput> executionHistoryByTrigger(String triggerName, String triggerGroup) {

		List<JobHistoryEntity> jobList = jobHistoryManager.findByTrigger(triggerName, triggerGroup);
		return schedulerServiceUtil.returnJobOutput(jobList);
	}

	//Execution History
	public List<GetJobOutput> executionHistory(SearchCriteria search,Pageable pageable) throws Exception {

		Page<JobHistoryEntity> foundJobs = jobHistoryManager.findAll(searchExecutionHistory(search),pageable);
		List<JobHistoryEntity> jobList = foundJobs.getContent();
		return schedulerServiceUtil.returnJobOutput(jobList);
	}
	
	public BooleanBuilder searchExecutionHistory(SearchCriteria search) throws Exception {

		QJobHistoryEntity jobHistory = QJobHistoryEntity.jobHistoryEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllPropertiesForJobHistory(jobHistory, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkPropertiesForJobHistory(keysList);
				return searchSpecificPropertyForJobHistory(jobHistory,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkPropertiesForJobHistory(keysList);
				return searchKeyValuePairForJobHistory(jobHistory, map);
			}
		}
		return null;
	}

	public BooleanBuilder searchAllPropertiesForJobHistory(QJobHistoryEntity jobHistory,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(jobHistory.jobName.likeIgnoreCase("%"+ value + "%"));
			builder.or(jobHistory.jobGroup.likeIgnoreCase("%"+ value + "%"));
			builder.or(jobHistory.triggerGroup.likeIgnoreCase("%"+ value + "%"));
			builder.or(jobHistory.triggerName.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals")) {
			builder.or(jobHistory.jobName.eq(value));
			builder.or(jobHistory.jobGroup.eq(value));
			builder.or(jobHistory.triggerGroup.eq(value));
			builder.or(jobHistory.triggerName.eq(value));

			Date date= stringToDate(value);
			if(date !=null) {
				builder.or(jobHistory.firedTime.eq(date));
				builder.or(jobHistory.finishedTime.eq(date));
			}
		}

		return builder;
	}

	public void checkPropertiesForJobHistory(List<String> list) throws Exception
	{
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("jobName"))
					|| (list.get(i).replace("%20","").trim().equals("jobGroup"))
					|| (list.get(i).replace("%20","").trim().equals("triggerName"))
					|| (list.get(i).replace("%20","").trim().equals("triggerGroup"))
					|| (list.get(i).replace("%20","").trim().equals("firedTime"))
					|| (list.get(i).replace("%20","").trim().equals("finishedTime"))))
			{
				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchSpecificPropertyForJobHistory(QJobHistoryEntity jobHistory,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("jobName")) {
				if(operator.equals("contains")) {
					builder.or(jobHistory.jobName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobHistory.jobName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("jobGroup")) {
				if(operator.equals("contains")) {
					builder.or(jobHistory.jobGroup.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobHistory.jobGroup.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerName")) {
				if(operator.equals("contains")) {
					builder.or(jobHistory.triggerName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobHistory.triggerName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("triggerGroup")) {
				if(operator.equals("contains")) {
					builder.or(jobHistory.triggerGroup.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(jobHistory.triggerGroup.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("firedTime")) {
				Date date= stringToDate(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(jobHistory.firedTime.eq(date));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("finishedTime")) {
				Date date= stringToDate(value);
				if(operator.equals("equals") && date != null ) {
					builder.or(jobHistory.finishedTime.eq(date));
				}
			}
		}
		return builder;
	}
	
	public BooleanBuilder searchKeyValuePairForJobHistory(QJobHistoryEntity jobHistory, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("jobName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobHistory.jobName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobHistory.jobName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobHistory.jobName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("jobGroup")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobHistory.jobGroup.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobHistory.jobGroup.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobHistory.jobGroup.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("triggerName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobHistory.triggerName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobHistory.triggerName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobHistory.triggerName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("triggerGroup")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(jobHistory.triggerGroup.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(jobHistory.triggerGroup.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(jobHistory.triggerGroup.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("firedTime")) {
				Date date= stringToDate(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(jobHistory.firedTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(jobHistory.firedTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Date startDate= stringToDate(details.getValue().getStartingValue());
					Date endDate= stringToDate(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(jobHistory.firedTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(jobHistory.firedTime.loe(endDate));
					else if(startDate!=null)
						builder.and(jobHistory.firedTime.goe(startDate));  
				}

			}
			if(details.getKey().replace("%20","").trim().equals("finishedTime")) {
				Date date= stringToDate(details.getValue().getSearchValue());
				if(details.getValue().getOperator().equals("equals") && date !=null)
					builder.and(jobHistory.finishedTime.eq(date));
				else if(details.getValue().getOperator().equals("notEqual") && date !=null)
					builder.and(jobHistory.finishedTime.ne(date));
				else if(details.getValue().getOperator().equals("range"))
				{
					Date startDate= stringToDate(details.getValue().getStartingValue());
					Date endDate= stringToDate(details.getValue().getEndingValue());
					if(startDate!=null && endDate!=null)	 
						builder.and(jobHistory.finishedTime.between(startDate,endDate));
					else if(endDate!=null)
						builder.and(jobHistory.finishedTime.loe(endDate));
					else if(startDate!=null)
						builder.and(jobHistory.finishedTime.goe(startDate));  
				}

			}
			
		}
		return builder;
	}

}