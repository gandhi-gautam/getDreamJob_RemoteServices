package in.getdreamjob.service.impl;

import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Company;
import in.getdreamjob.model.Job;
import in.getdreamjob.repository.CompanyRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    public static final int PAGE_SIZE = 100;
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
    public Company createNewJob(long companyId, Job job) throws ParseException {
        Optional<Company> optional = companyRepository.findById(companyId);
        if (optional.isPresent()) {
            Company company = optional.get();
            job.setCreatedOn(new Date());
            job.setCompany(company);
            company.getJobs().add(job);
            return companyRepository.save(company);
        }
        throw new ResourceNotFoundException("Company with id: " + companyId + " Not Found");
    }

    @Override
    public Job updateJob(long jobId, Job job) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isPresent()) {
            Job actualJob = optionalJob.get();
            validateJobData(actualJob, job);
            return jobRepository.save(actualJob);
        }
        throw new ResourceNotFoundException("Job with id: " + jobId + " Not Found");
    }

    @Override
    public Page<Job> getAllJobs(int pageNo) {
        PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
        Page<Job> jobs = jobRepository.findAll(request);
        for (Job job : jobs) {
            if (job.getCompany() != null) {
                addCompanyDetails(job);
            }
        }
        return jobs;
    }

    @Override
    public Job getJob(long jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            if (job.getCompany() != null) {
                addCompanyDetails(job);
            }
            return job;
        }
        throw new ResourceNotFoundException("Job with id: " + jobId + " not found");
    }

    @Override
    public Object deleteJob(long jobId) {

        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setCompany(null);
            jobRepository.delete(job);
        } else {
            throw new ResourceNotFoundException("Job with id: " + jobId + " not found");
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
            actualJob.setMaxSalary(job.getMaxSalary());
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

    /**
     * This method fetched company details from the database and set inside the job
     *
     * @param job
     */
    public void addCompanyDetails(Job job) {
        if (job.getCompany().getId() != 0) {
            job.setCompanyId(job.getCompany().getId());
        }
        if (job.getCompany().getName() != null && !job.getCompany().getName().isEmpty()) {
            job.setCompanyName(job.getCompany().getName());
        }

        if (job.getCompany().getOfficialWebsite() != null && !job.getCompany().getOfficialWebsite().isEmpty()) {
            job.setCompanyOfficialWebsite(job.getCompany().getOfficialWebsite());
        }
    }
}
