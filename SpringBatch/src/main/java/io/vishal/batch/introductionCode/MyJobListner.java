package io.vishal.batch.introductionCode;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListner implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("----------------------Job started --> "+jobExecution.getStatus().toString());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("----------------------Job ended --> "+jobExecution.getStatus().toString());
	}

}
