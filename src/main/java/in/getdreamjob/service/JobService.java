package in.getdreamjob.service;

import in.getdreamjob.model.Company;
import in.getdreamjob.model.Job;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface JobService {
    public Company createNewJob(long companyId, Job job) throws ParseException;

    public Job updateJob(long jobId, Job job);

    public Page<Job> getAllJobs(int pageNo);

    public Job getJob(long jobId);

    Object deleteJob(long jobId);
}
