

import org.springframework.core.env.Environment;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.excel.poi.PoiItemReader;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.core.io.UrlResource;
import springbatch.first_project.FirstProjectApplication;
import springbatch.first_project.config.CourseUtils;
import springbatch.first_project.config.PersonExcelRowMapper;
import springbatch.first_project.entity.AnonymizePerson;
import springbatch.first_project.entity.Person;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { StepScopeTest2.TestConfig.class})
class StepScopeTest2 {
	private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH = "excel.to.database.job.source.file.path";
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void runJob() throws Exception {
		{
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("inputPath", "person89.xlsx")
					.addParameter("outputPath", new JobParameter("output/chunkoutput.json"))

					.toJobParameters();

			JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		}


	}

	@Configuration
	@EnableBatchProcessing
	static class TestConfig {
		

		@Autowired
		private JobBuilderFactory jobBuilderFactory;

		@Autowired
		private StepBuilderFactory stepBuilderFactory;
		
		@Bean
		public JobLauncherTestUtils utils(){
			return new JobLauncherTestUtils();
		}

		@Bean
		public Job job(Step step) throws IOException {
			System.out.println("job");
		    byte[] array = new byte[5]; // length is bounded by 7
		    new Random().nextBytes(array);
		    String generatedString = new String(array, Charset.forName("UTF-8"));
			return jobBuilderFactory.get(generatedString).start(step).build();
		}

		@Bean
		public Step step(JdbcBatchItemWriter <AnonymizePerson> writer
				,ItemProcessor<Person, AnonymizePerson> processor
				,PoiItemReader<Person> reader) throws IOException {
			System.out.println("step");
			Step step = stepBuilderFactory
					.get("step").<Person, AnonymizePerson>chunk(3)
					.reader(reader)
					.processor(processor)
					.writer(writer)
					.build();

			return step;
		}

		@Bean
		@StepScope
		public ItemProcessor<Person, AnonymizePerson> processor() {
			System.out.println("processor");

			return new ItemProcessor<Person, AnonymizePerson>() {
				@Override
				public AnonymizePerson process(Person person) throws Exception {
					AnonymizePerson anonymizePerson = new AnonymizePerson();
					if (!person.customer) {
						return null;
					}
					anonymizePerson.name = "John Dohe";
					anonymizePerson.customer = person.customer;
					anonymizePerson.birthday = person.birthday;
					anonymizePerson.revenue = person.revenue;
					return anonymizePerson;
				}
			};
		}

		@Bean
		@StepScope
		public PoiItemReader<Person> reader(@Value("#{jobParameters[inputPath]}") String inputPath) {
			System.out.println("ItemReader");
			PoiItemReader<Person> reader = new PoiItemReader<Person>();
			reader.setName("readername");
			reader.setLinesToSkip(1);
			reader.setResource(new ClassPathResource(inputPath));
			reader.setRowMapper(new PersonExcelRowMapper());
			return reader;
		}
//		
//		@Bean
//		@StepScope
//		public ItemReader<Person> reader(Environment environment){
//			System.out.println("ItemReader");
//	        PoiItemReader<Person> reader = new PoiItemReader<Person>();
//	        reader.setName("neadername");
//	        reader.setLinesToSkip(1);
//	        reader.setResource(new ClassPathResource(environment.getProperty(PROPERTY_EXCEL_SOURCE_FILE_PATH)));
//	        reader.setRowMapper(new PersonExcelRowMapper());
//	        return reader;
//	    }

		@Bean
		@StepScope
		public JdbcBatchItemWriter <AnonymizePerson> writer(DataSource datasource){
			System.out.println("Writer");

		 JdbcBatchItemWriterBuilder<AnonymizePerson> namedParametersJdbcTemplate =  new JdbcBatchItemWriterBuilder<AnonymizePerson>()
					.namedParametersJdbcTemplate(new NamedParameterJdbcTemplate(datasource));
		 JdbcBatchItemWriterBuilder<AnonymizePerson> itemSqlParameterSourceProvider  =  namedParametersJdbcTemplate
					.assertUpdates(true)
					.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<AnonymizePerson>());
			return itemSqlParameterSourceProvider
					.sql("INSERT INTO anonymize_person (ID, NAME, BIRTHDAY,REVENUE,CUSTOMER) VALUES(:id, :name, :birthday, :revenue, :customer)")
					.build();
		}
	    @Bean
	    @Primary
	    public static final DataSource dataSource() {
	        return DataSourceBuilder
	            .create()
	            .username("root")
	            .password("1234")
	            .url("jdbc:mysql://localhost:3306/batch")
	            .driverClassName("com.mysql.cj.jdbc.Driver")
	            .build();
	    }
	}

}
