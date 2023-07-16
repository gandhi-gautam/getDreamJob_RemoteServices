package in.getdreamjob.cron;

import in.getdreamjob.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
@EnableScheduling
public class DisableJob {

    private final static Logger logger = LoggerFactory.getLogger(DisableJob.class);
    private final JobRepository jobRepository;
    private final Executor taskExecutor = Executors.newFixedThreadPool(5); // Number of threads

    @Autowired
    public DisableJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * This method is a cron job that runs everyday at midnight and set disable flag the jobs where last apply date is
     * already past and isDisable flag is set true
     */
    @Async
    @Scheduled(cron = "0 54 8 * * ?", zone = "Asia/Kolkata")
    public void toggleDisableFlag() {
        taskExecutor.execute(() -> {
            logger.info("Inside toggleDisableFlag cron job");
            LocalDateTime currentDate = LocalDateTime.now();
            logger.info("Updating job for current date: " + currentDate);
            jobRepository.updateDisableFlag(currentDate);
        });
    }
}
