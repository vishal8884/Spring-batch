package io.vishal.batch.CSV_TO_DB.config;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.vishal.batch.CSV_TO_DB.model.Product;

@Configuration
public class BatchConfig {

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
		
		return reader;
	}
}
