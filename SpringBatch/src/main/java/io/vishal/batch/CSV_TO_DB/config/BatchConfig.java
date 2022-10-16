package io.vishal.batch.CSV_TO_DB.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.vishal.batch.CSV_TO_DB.model.Product;

@Configuration
public class BatchConfig {
	
	@Autowired
	private StepBuilderFactory sbf;
	
	@Autowired
	private JobBuilderFactory jbf;
	
	
	public Job job() {
		return jbf.get("s2")
				.incrementer(new RunIdIncrementer())
				.start(step())
				.build();
	}
	
	
	public Step step() {
		return sbf.get("s1")
				.<Product,Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public ItemReader<Product> reader(){
		FlatFileItemReader<Product> reader = new FlatFileItemReader<>();      //responsible for reading csv and converting into a java object
		reader.setResource(new ClassPathResource("products.csv"));
		
		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();    // responsible for mapping each line into a product object
		
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(); // responsible for reading each token seperated by comma
		lineTokenizer.setNames("id","name","description","price");
		
		BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>(); //responsible for taking the values in each of the variables
		fieldSetMapper.setTargetType(Product.class);
		
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		reader.setLineMapper(lineMapper);
		return reader;
	}
	
	@Bean
	public ItemProcessor<Product,Product> processor() {
		return (p) -> {
			p.setPrice(p.getPrice() - p.getPrice()*10/100);
			return p;
		};
	}
	
	
	@Bean
	public ItemWriter<Product> writer(){
		JdbcBatchItemWriter<Product> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setDataSource(dataSource());
		writer.setSql("INSERT INTO PRODUCT (ID,NAME,DESCRIPTION,PRICE) VALUES (:id,:name,:description,:price)");
		
		return writer;
	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/springBatch");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		
		return dataSource;
	}
}




























