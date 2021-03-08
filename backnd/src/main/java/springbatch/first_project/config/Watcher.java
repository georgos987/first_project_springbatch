package springbatch.first_project.config;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import springbatch.first_project.jobs.AnonymizeJob;

@Component
public class Watcher {

	@Autowired
	private AnonymizeJob anonymizeJob;

	public final String DATA_DIRECTORY = "C:\\Users\\gMakhool\\OneDrive - Consileon\\Desktop\\sprinbatch\\first_project_springbatch\\backnd\\src\\main\\resources\\data";
			
	@EventListener(value = ApplicationReadyEvent.class)
	public void WatcherAfterApplicationStarted() throws Exception {
		System.out.println(DATA_DIRECTORY);
		WatchAlreadyFileInDataDirectory();
		 WatcherForNewEntry();
	}

	public void WatchAlreadyFileInDataDirectory() {
		FileUtils.listFiles(new File(DATA_DIRECTORY), new String[] { "xlsx" }, false).forEach(t -> {
			try {
				anonymizeJob.runJob(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void WatcherForNewEntry() throws Exception {

        FileAlterationObserver observer = new FileAlterationObserver(DATA_DIRECTORY);
        FileAlterationMonitor monitor = new FileAlterationMonitor(5_000);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
            	try {
					anonymizeJob.runJob(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        monitor.addObserver(observer);
        monitor.start();	
        }

}
