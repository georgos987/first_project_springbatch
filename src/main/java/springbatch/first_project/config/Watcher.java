package springbatch.first_project.config;

import org.hibernate.engine.jdbc.batch.spi.BatchObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import springbatch.first_project.jobs.AnonymizeJob;

@Component
public class Watcher {
	
	@Autowired
	private AnonymizeJob anonymizeJob;
	
	@EventListener(value = ApplicationReadyEvent.class)
	public void  WatcherAfterApplicationStarted() throws Exception {
		
		anonymizeJob.runJob();
	}
	
	public void  WatcherForNewEntry() throws Exception {
		
		//BatchObserver x = new BatchObserver();
	}

}
