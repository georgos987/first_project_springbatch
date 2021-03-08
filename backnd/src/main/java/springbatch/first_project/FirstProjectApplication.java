package springbatch.first_project;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


import springbatch.first_project.jobs.AnonymizeJob;
@EntityScan(basePackageClasses = { 
		FirstProjectApplication.class
})
@Configuration
@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackageClasses = FirstProjectApplication.class)
public class FirstProjectApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) throws BeansException, Exception {
		ConfigurableApplicationContext context = SpringApplication.run(FirstProjectApplication.class, args);

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
