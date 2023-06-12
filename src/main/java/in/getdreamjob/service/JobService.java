package in.getdreamjob.service;

import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;

public interface JobService {
    public GeneralResponse createNewJob(long companyId, Job job);

    public GeneralResponse updateJob(Job job);

    public GeneralResponse getAllJobs(int pageNo);

    public GeneralResponse getJob(long jobId);

    public GeneralResponse deleteJob(long jobId);
}
