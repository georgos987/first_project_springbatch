package springbatch.first_project.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AnonymizeJob {
	
	@Autowired
	private Job job;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	public void runJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addParameter("inputPath", new JobParameter("classpath:person.csv"))
				.toJobParameters();
		
		jobLauncher.run(job,jobParameters);
	}

}
