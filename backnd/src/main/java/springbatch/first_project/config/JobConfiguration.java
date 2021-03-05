package springbatch.first_project.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import springbatch.first_project.entity.AnonymizePerson;
import springbatch.first_project.entity.Person;
//import springbatch.first_project.service.AnonymizePersonService;
//import springbatch.first_project.service.PersonService;

@Configuration
public class JobConfiguration {

//	@Autowired
//	PersonService personService;
//	
//	
//	@Autowired
//	AnonymizePersonService anonymizePersonService;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("myjob201")
				.start(step())
				.listener(new CourseUtilJobSummaryListener())
				.build();
	}
	
	
	@Bean
	public Step step() {
	Step step = stepBuilderFactory.get("step")
		.<Person,AnonymizePerson>chunk(1)
	
		.reader(reader(null))
		.processor(processor(null))
		.writer(writer(null))
		.build();
		System.out.println(step);
		return step;
	}
	

	
	@Bean
	@StepScope
    ItemReader<Person> reader(@Value("#{jobParameters['inputPath']}") String inputPath) {
        PoiItemReader<Person> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource(inputPath));
        reader.setRowMapper(excelRowMapper());
        return reader;
    }
    private RowMapper<Person> excelRowMapper() {
        BeanWrapperRowMapper<Person> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(Person.class);
        return rowMapper;
    }
	
	@Bean
	@StepScope
	public ItemProcessor<Person,AnonymizePerson> processor(DataSource datasource) {
		return new ItemProcessor<Person,AnonymizePerson>(){
			@Override
			public AnonymizePerson process(Person person) throws Exception {
				AnonymizePerson anonymizePerson = new AnonymizePerson();
				if(!person.customer) {
					return null;
				}				
					anonymizePerson.name = "John Dohe";
					anonymizePerson.customer=person.customer;
					anonymizePerson.birthday=person.birthday;
					anonymizePerson.revenue= person.revenue;
				return anonymizePerson;
			}
		};
	}
	
	@Bean
	@StepScope
	public JdbcBatchItemWriter <AnonymizePerson> writer(DataSource datasource){
	 JdbcBatchItemWriterBuilder<AnonymizePerson> namedParametersJdbcTemplate =  new JdbcBatchItemWriterBuilder<AnonymizePerson>()
				.namedParametersJdbcTemplate(new NamedParameterJdbcTemplate(datasource));
	 JdbcBatchItemWriterBuilder<AnonymizePerson> itemSqlParameterSourceProvider  =  namedParametersJdbcTemplate
				.assertUpdates(true)
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<AnonymizePerson>());
	  
		return itemSqlParameterSourceProvider
				.sql("INSERT INTO anonymize_person (ID, NAME, BIRTHDAY,REVENUE,CUSTOMER) VALUES(:id, :name, :birthday, :revenue, :customer)")
				.build();
	}
}
