package com.nfinity.ll.testaz.scheduler.jobs;

import org.quartz.*;

public class sampleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey key = context.getJobDetail().getKey();

	      JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        //  dataMap.put("NumTries", 3);
	      String jobValue = dataMap.getString("jobSays");

	      System.out.println("Instance " + key + " have value " + jobValue + "job run time ");
	      try {
	          Thread.sleep(2000);
	      } catch (InterruptedException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	      }
		
	}

}

