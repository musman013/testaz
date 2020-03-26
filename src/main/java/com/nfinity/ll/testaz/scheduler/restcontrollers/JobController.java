package com.nfinity.ll.testaz.scheduler.restcontrollers;

import com.nfinity.ll.testaz.scheduler.schedulerservice.SchedulerService;
import com.nfinity.ll.testaz.commons.search.SearchCriteria;
import com.nfinity.ll.testaz.commons.search.SearchUtils;
import com.nfinity.ll.testaz.commons.application.OffsetBasedPageRequest;
import com.nfinity.ll.testaz.commons.domain.EmptyJsonResponse;
import com.nfinity.ll.testaz.scheduler.application.dto.*;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/jobs")
public class JobController {

	@Autowired
	SchedulerService schedulerService;

	@Autowired
	private Environment env;

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<JobListOutput>> listAllJobs(@RequestParam(value = "search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		//if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable offsetPageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		List<JobListOutput> list = schedulerService.listAllJobs(searchCriteria,offsetPageable);

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/getJobGroups", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listAllJobGroups() throws SchedulerException, IOException {
		List<String> list = schedulerService.listAllJobGroups();

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<JobDetails> returnJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		JobDetails detail=schedulerService.returnJob(jobName, jobGroup);
        if(detail == null)
        {
        	throw new EntityNotFoundException(
					String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
        }
		
		return new ResponseEntity(detail, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/getJobClasses", method = RequestMethod.GET)
	public ResponseEntity<List<String>> listAllJobClasses() throws SchedulerException {
		List<String> list = schedulerService.listAllJobClasses();

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/executingJobs", method = RequestMethod.GET)
	public ResponseEntity<List<GetExecutingJob>> listCurrentlyExecutingJobs() throws SchedulerException {
		List<GetExecutingJob> list = schedulerService.currentlyExecutingJobs();
       
		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_DELETE')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.deleteJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_UPDATE')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> updateJob(@PathVariable String jobName, @PathVariable String jobGroup, @RequestBody @Valid JobDetails obj) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		obj.setJobName(jobName);
		obj.setJobGroup(jobGroup);
		boolean status = schedulerService.updateJob(obj);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/pauseJob/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> pauseJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.pauseJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/resumeJob/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> resumeJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.resumeJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JobDetails> createJob(@RequestBody @Valid JobDetails obj) throws SchedulerException, ClassNotFoundException {
		if(obj.getJobClass() == null || obj.getJobName() == null || obj.getJobGroup() == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = schedulerService.createJob(obj);
		if(status==false)
		{
			throw new EntityExistsException(
					String.format("There already exists a job with a jobName=%s and jobGroup=%s", obj.getJobName() ,obj.getJobGroup()));
    }
		
		return new ResponseEntity(obj, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}/triggers", method = RequestMethod.GET)
	public ResponseEntity<List<TriggerDetails>> returnTriggerForJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		List<TriggerDetails> triggerDetails = schedulerService.returnTriggersForAJob(new JobKey(jobName, jobGroup));
		if(triggerDetails==null)
		{
			throw new EntityNotFoundException(
					String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(triggerDetails, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}/jobExecutionHistory", method = RequestMethod.GET)
	public ResponseEntity<List<GetJobOutput>> executionHistoryByJob(@PathVariable String jobName, @PathVariable String jobGroup) {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		List<GetJobOutput> list = schedulerService.executionHistoryByJob(jobName, jobGroup);
		return new ResponseEntity(list, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value= "/jobExecutionHistory", method = RequestMethod.GET)
	public ResponseEntity<List<GetJobOutput>> executionHistory (@RequestParam(value = "search", required=false) String search,@RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception
	{
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		//if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable offsetPageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		return new ResponseEntity(schedulerService.executionHistory(searchCriteria,offsetPageable),HttpStatus.OK);

	}


}
