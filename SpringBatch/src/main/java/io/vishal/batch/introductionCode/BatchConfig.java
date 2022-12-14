package io.vishal.batch.introductionCode;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
	
	@Autowired
	private StepBuilderFactory sbf;
	
	@Autowired
	private JobBuilderFactory jbf;
	
	@Bean  //when we autowire Job interface then it will automatically inject the below
	public Job job() {
		return jbf.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(listiner())
				.start(step())
				.build();
	}
	
	@Bean
	public Step step() {
		return sbf.get("step1")
				.<String,String>chunk(3)   //chunck size no of chunks for read,process,write
				.reader(reader())
				.writer(writer())
				.processor(processor())
				.build();
	}

	@Bean
	public Reader reader() {
		return new Reader();
	}
	
	@Bean
	public Writer writer() {
		return new Writer();
	}
	
	@Bean
	public Processor processor() {
		return new Processor();
	}
	
	@Bean
	public MyJobListner listiner() {
		return new MyJobListner();
	}
}
