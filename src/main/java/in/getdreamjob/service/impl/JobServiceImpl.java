package in.getdreamjob.service.impl;

import in.getdreamjob.model.Company;
import in.getdreamjob.model.Job;
import in.getdreamjob.repository.CompanyRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private CompanyRepository companyRepository;
    private JobRepository jobRepository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Company createNewJob(long companyId, Job job) {
        if (companyRepository.existsById(companyId)) {
            Company company = companyRepository.findById(companyId).get();
            job.setCreatedOn(new java.util.Date());
            job.setCompany(company);
            company.getJobs().add(job);
            return companyRepository.save(company);
        }
        return null;
    }

    @Override
    public Job updateJob(long jobId, Job job) {
        if (jobRepository.existsById(jobId)) {
            Job actualJob = jobRepository.findById(jobId).get();
            validateJobData(actualJob, job);
            return jobRepository.save(actualJob);
        }
        return null;
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        for (Job job : jobs) {
            if (job.getCompany() != null) {
                job.setCompanyId(job.getCompany().getId());
            }
        }
        return jobs;
    }

    @Override
    public Job getJob(long jobId) {
        if (jobRepository.existsById(jobId)) {
            Job job = jobRepository.findById(jobId).get();
            if (job.getCompany() != null) {
                job.setCompanyId(job.getCompany().getId());
            }
            return job;
        }
        return null;
    }

    private void validateJobData(Job actualJob, Job job) {
        if (job.getProfileName() != null && !job.getProfileName().isEmpty()) {
            actualJob.setProfileName(job.getProfileName());
        }
        if (job.getNoOfOpening() != 0) {
            actualJob.setNoOfOpening(job.getNoOfOpening());
        }
        if (job.getBatchEligible() != null && !job.getBatchEligible().isEmpty()) {
            actualJob.setBatchEligible(job.getBatchEligible());
        }
        if (job.getMinSalary() != null && !job.getMinSalary().isEmpty()) {
            actualJob.setMinSalary(job.getMinSalary());
        }
        if (job.getMaxSalary() != null && !job.getMaxSalary().isEmpty()) {
            actualJob.setMinSalary(job.getMaxSalary());
        }
        if (job.getApplicationMode() != null && !job.getApplicationMode().isEmpty()) {
            actualJob.setApplicationMode(job.getApplicationMode());
        }
        if (job.getLastApplyDate() != null) {
            actualJob.setLastApplyDate(job.getLastApplyDate());
        }
        if (job.getApplyLink() != null && !job.getApplyLink().isEmpty()) {
            actualJob.setApplyLink(job.getApplyLink());
        }
        if (job.getJobDescription() != null && !job.getJobDescription().isEmpty()) {
            actualJob.setJobDescription(job.getJobDescription());
        }
        if (job.getBasicQualification() != null && !job.getBasicQualification().isEmpty()) {
            actualJob.setBasicQualification(job.getBasicQualification());
        }
        if (job.getPreferredQualification() != null && !job.getPreferredQualification().isEmpty()) {
            actualJob.setPreferredQualification(job.getPreferredQualification());
        }
        if (job.getImage() != null && job.getImage().length > 0) {
            actualJob.setImage(job.getImage());
        }
    }
}
