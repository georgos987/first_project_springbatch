package springbatch.first_project.config;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.Random;

import javax.sql.DataSource;


import org.springframework.batch.core.Job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import springbatch.first_project.entity.AnonymizePerson;
import springbatch.first_project.entity.Person;
@Configuration
public class JobConfiguration {

	 private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH = "excel.to.database.job.source.file.path";
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;

	
	@Bean
	public Job job(Step step) throws IOException {
	    byte[] array = new byte[5]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
		return jobBuilderFactory.get(generatedString)
				.start(step())
				.listener(new JobSummaryListener())
				.build();
	}
	
	
	@Bean
	public Step step() throws IOException {
	Step step = stepBuilderFactory
		.get("step")
		.<Person,AnonymizePerson>chunk(1)
		.reader(reader(null))
		.processor(processor())
		.writer(writer(null))
		.build();
	
		return step;
	}
	// after return reader the job is finish, no calling processor or writer
	@Bean
	@StepScope
	public PoiItemReader<Person> reader(@Value("#{jobParameters['inputPath']}") String inputPath) throws FileNotFoundException{
		System.out.println("ItemReader");
        PoiItemReader<Person> reader = new PoiItemReader<Person>();
        PushbackInputStream input = new PushbackInputStream(new FileInputStream(inputPath));
        InputStreamResource resource = new InputStreamResource(input);
        reader.setName("readername");
        reader.setLinesToSkip(1);
        reader.setResource(resource);
        reader.setRowMapper(new PersonExcelRowMapper());
        return reader;
    }
	// this is working without StpeScope
//	@Bean
//	public ItemReader<Person> reader(Environment environment){
//        PoiItemReader<Person> reader = new PoiItemReader<Person>();
//        reader.setName("neadername");
//        reader.setLinesToSkip(1);
//        reader.setResource(new ClassPathResource(environment.getProperty(PROPERTY_EXCEL_SOURCE_FILE_PATH)));
//        reader.setRowMapper(new PersonExcelRowMapper());
//        return reader;
//    }

	

    
	@Bean
	@StepScope
	public ItemProcessor<Person,AnonymizePerson> processor() {
		System.out.println("processor");

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
		System.out.println("itemWriter");

	 JdbcBatchItemWriterBuilder<AnonymizePerson> namedParametersJdbcTemplate =  new JdbcBatchItemWriterBuilder<AnonymizePerson>()
				.namedParametersJdbcTemplate(new NamedParameterJdbcTemplate(datasource));
	 JdbcBatchItemWriterBuilder<AnonymizePerson> itemSqlParameterSourceProvider  =  namedParametersJdbcTemplate
				.assertUpdates(true)
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<AnonymizePerson>());
	  System.out.println("Ende den job");
		return itemSqlParameterSourceProvider
				.sql("INSERT INTO anonymize_person (ID, NAME, BIRTHDAY,REVENUE,CUSTOMER) VALUES(:id, :name, :birthday, :revenue, :customer)")
				.build();
	}
}
