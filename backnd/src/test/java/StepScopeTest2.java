


import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import springbatch.first_project.config.PersonExcelRowMapper;
import springbatch.first_project.entity.AnonymizePerson;
import springbatch.first_project.entity.Person;

import java.io.IOException;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { StepScopeTest2.TestConfig.class})
class StepScopeTest2 {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void runJob() throws Exception {
		{
			JobParameters jobParameters = new JobParametersBuilder()
					.addParameter("inputPath", new JobParameter("classpath:person.xlxs"))
					.addParameter("outputData", new JobParameter("output/chunkoutput.xlxs"))

					.toJobParameters();

			JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		}


	}

	@SuppressWarnings("WeakerAccess")
	@Configuration
	static class TestConfig {

		@Autowired
		private JobBuilderFactory jobBuilderFactory;

		@Autowired
		private StepBuilderFactory stepBuilderFactory;

		@Bean
		public Job job() throws IOException {
			return jobBuilderFactory.get("myJob").start(step()).build();
		}

		@Bean
		public Step step() throws IOException {
			Step step = stepBuilderFactory
					.get("step").<Person, AnonymizePerson>chunk(1)
					.reader(reader(null))
					.processor(processor())
					.writer(writer(null))
					.build();

			return step;
		}

		
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
		public ItemReader<Person> reader(@Value("#{jobParameters['inputPath']}") String inputPath) {
			System.out.println("ItemReader");
			PoiItemReader<Person> reader = new PoiItemReader<Person>();
			reader.setName("readername");
			reader.setLinesToSkip(1);
			reader.setResource(new ClassPathResource(inputPath));
			reader.setRowMapper(new PersonExcelRowMapper());
			return reader;
		}

		@Bean
		@StepScope
		public JdbcBatchItemWriter<AnonymizePerson> writer(DataSource datasource) {
			System.out.println("itemWriter");

			JdbcBatchItemWriterBuilder<AnonymizePerson> namedParametersJdbcTemplate = new JdbcBatchItemWriterBuilder<AnonymizePerson>()
					.namedParametersJdbcTemplate(new NamedParameterJdbcTemplate(datasource));
			JdbcBatchItemWriterBuilder<AnonymizePerson> itemSqlParameterSourceProvider = namedParametersJdbcTemplate
					.assertUpdates(true)
					.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<AnonymizePerson>());
			System.out.println("Ende den job");
			return itemSqlParameterSourceProvider.sql(
					"INSERT INTO anonymize_person (ID, NAME, BIRTHDAY,REVENUE,CUSTOMER) VALUES(:id, :name, :birthday, :revenue, :customer)")
					.build();
		}
	}

}
