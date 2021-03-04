package springbatch.first_project;



import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import springbatch.first_project.config.JobConfiguration;
import springbatch.first_project.entity.AnonymizePerson;
import springbatch.first_project.entity.Person;
//import springbatch.first_project.service.PersonService;
import springbatch.first_project.jobs.AnonymizeJob;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;


@SpringBootTest(classes = {JobConfiguration.class, FirstProjectApplicationTests.TestConfig.class
		})

class FirstProjectApplicationTests {
	
	@MockBean
	JdbcCursorItemReader<Person> jdbcCursorItemReader;
	
	@MockBean
	JdbcBatchItemWriter<AnonymizePerson> jdbcBatchItemWriter;
	
//	@MockBean
//	JobRepository jobRepository;
	
	public DataSource datasource;

	public ConfigurableApplicationContext context;
	
//	@Autowired
//	private JobRepositoryTestUtils jobRepositoryTestUtils;

//	@Before
//	public void setUp() {
//		this.context = new AnnotationConfigApplicationContext(TestConfig.class);
//		this.datasource = (DataSource) context.getBean("datasource");
//	}
//
//	@After
//	public void tearDown() {
//		if (this.context != null) {
//			this.context.close();
//		}
//	}
	
	@Before
	public void setUp() {
		Person person1 = new Person("Ahmad", "2000-01-02", "Ahmad.com", 99000, true);
		BeanPropertyRowMapper BPR = new BeanPropertyRowMapper(Person.class);
	
	}
	

	@Autowired
	private Job job;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	JobConfiguration jobConfiguration = EasyMock.createMock(JobConfiguration.class);
	
	@Test
	void readerTest1() {
		JobConfiguration jobConfiguration1= new JobConfiguration();
		Person person1 = new Person("Ahmad", "2000-01-02", "Ahmad.com", 99000, true);
		IExpectationSetters<JdbcCursorItemReader<Person>> x = EasyMock.expect(jobConfiguration.reader(datasource)).andReturn(jdbcCursorItemReader);
		System.out.println(x+"111111111111");
	}
	
	
	@Test
	@Disabled
	void jobTest() throws Exception {
		AnonymizeJob anonymizeJob = EasyMock.createMock(AnonymizeJob.class);
		Person person1 = new Person("Ahmad", "2000-01-02", "Ahmad.com", 99000, true);

		AnonymizePerson anonymizePerson = new AnonymizePerson("John", "2000-01-02", 99000, true);
		
		//EasyMock.expect(anonymizeJob.runJob())
		

		List<Person> listPersons = new ArrayList<Person>();
		listPersons.add(person1);
		//Mockito.when(jdbcCursorItemReader.read()).thenReturn(person1).thenReturn(null);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addParameter("persons", new JobParameter(listPersons.toString())).toJobParameters();
		

		//JobExecution jobExcution = jobLauncherTestUtils.launchJob(jobParameters);
		//Mockito.when(jobRepositoryTestUtils.createJobExecutions(1))
		//.thenReturn(List.of()).thenReturn(null);
		//BatchStatus exitStatus = jobExcution.getStatus();
		//assertThat(exitStatus).isEqualTo(BatchStatus.COMPLETED);
	}
	
	@Test
	@Disabled
	void readerTest() throws UnexpectedInputException, ParseException, Exception {
		EasyMock.expect(jobConfiguration.job()).andReturn(job);
		
		EasyMock.expect(jobConfiguration.reader(datasource)).andReturn(jdbcCursorItemReader);
		
		EasyMock.expect(jobConfiguration.writer(datasource)).andReturn(jdbcBatchItemWriter);
		
		EasyMock.replay(jobConfiguration);

		
	}
	
//		 @Test 
//		 void processorTest() {
//			 EasyMock.expect(createMock.processor(datasource)).andReturn
//			 
//		 }

	@Configuration
	@EnableBatchProcessing
	static class TestConfig {

		@Bean
		public JobLauncherTestUtils jobLauncherTestUtils() {
			return new JobLauncherTestUtils();
		}
	    @Bean
	    @Primary
	    public DataSource dataSource() {
	        return DataSourceBuilder
	            .create()
	            .username("user")
	            .password("1234")
	            .url("jdbc:h2:file:C:/Users/georg/demo/testdb7;DB_CLOSE_ON_EXIT=TRUE")
	            .driverClassName("org.h2.Driver")
	            .build();
	    }

	}

}
