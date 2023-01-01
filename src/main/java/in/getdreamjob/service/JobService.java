package in.getdreamjob.service;

import in.getdreamjob.model.Company;
import in.getdreamjob.model.Job;

import java.util.List;

public interface JobService {
    public Company createNewJob(long companyId, Job job);

    public Job updateJob(long jobId, Job job);

    public List<Job> getAllJobs();

    public Job getJob(long jobId);
}
