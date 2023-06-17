package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Category;
import in.getdreamjob.model.Company;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.repository.CategoryRepository;
import in.getdreamjob.repository.CompanyRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.service.JobService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {
    public static final int PAGE_SIZE = 10;
    public static final int MONTHS = 1;
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ResponseUtil responseUtil;
    private final CategoryRepository categoryRepository;

    @Autowired
    public JobServiceImpl(CompanyRepository companyRepository, JobRepository jobRepository, ResponseUtil responseUtil,
                          CategoryRepository categoryRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.responseUtil = responseUtil;
        this.categoryRepository = categoryRepository;
    }

    /**
     * This method creates new job and connects with company
     *
     * @param companyId
     * @param job
     * @return
     */
    @Override
    public GeneralResponse createNewJob(long companyId, Job job) {
        logger.info("Inside createNewJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job Not Created!");
        if (companyId > 0L) {
            checkJobMandatoryData(job);
            Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isEmpty()) {
                throw new ResourceNotFoundException("Company with Id: " + companyId + " Not Found!");
            }

            checkJobApplyLinkUniqueness(job.getApplyLink());
            ZoneId indianZone = ZoneId.of("Asia/Kolkata");
            ZonedDateTime currentDateTime = ZonedDateTime.now(indianZone);
            job.setCreatedOn(currentDateTime.toLocalDateTime());
            job.setLastApplyDate(job.getCreatedOn().plusMonths(MONTHS));
            assignCategoryBasedOnExperience(job);
            job = jobRepository.save(job);
            response.setStatus("Success");
            response.setData(job);
            response.setMessage("Job Saved");
        } else {
            throw new EmptyFieldException("Company Id Not Present Payload!");
        }
        return response;
    }

    /**
     * This method updates job data based on id
     *
     * @param job
     * @return
     */
    @Override
    public GeneralResponse updateJob(Job job) {
        logger.info("Inside updateJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job Not Updated!");
        if (job != null && job.getId() != null) {
            if (job.getId() > 0) {
                Optional<Job> optionalJob = jobRepository.findById(job.getId());
                if (optionalJob.isEmpty()) {
                    throw new ResourceNotFoundException("Job with Id: " + job.getId() + " Not Found!");
                }
                Job actualJob = optionalJob.get();
                compareJobDate(actualJob, job);
                actualJob = jobRepository.save(actualJob);
                response.setMessage("Job Updated!");
                response.setData(actualJob);
                response.setStatus("Success");
            } else {
                throw new EmptyFieldException("Valid Job Id Not Present In the Payload!");
            }
        } else {
            throw new EmptyFieldException("Job Data Or Job Id is Not Present In The Payload!");
        }
        return response;
    }

    /**
     * This method fetches all the job and returns paginated result
     *
     * @param pageNo
     * @return
     */
    @Override
    public GeneralResponse getAllJobs(int pageNo) {
        logger.info("Inside getAllJobs service method");
        GeneralResponse response = responseUtil.createResponseObject("No Job Data Found");
        PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
        Page<Job> jobs = jobRepository.findAll(request);
        if (jobs.getSize() > 0) {
            response.setStatus("Success");
            response.setData(jobs);
            response.setMessage("Jobs Found!");
        }
        return response;
    }

    /**
     * This method get job data based on id
     *
     * @param jobId
     * @return
     */
    @Override
    public GeneralResponse getJob(long jobId) {
        logger.info("Inside getJob service method");
        GeneralResponse response = responseUtil.createResponseObject("No Job Found");
        if (jobId > 0L) {
            Optional<Job> jobOptional = jobRepository.findById(jobId);
            if (jobOptional.isEmpty()) {
                throw new ResourceNotFoundException("Job With Id: " + jobId + " Not Found!");
            }
            Job job = jobOptional.get();
            if (job != null) {
                response.setMessage("Job Found!");
                response.setData(job);
                response.setStatus("Success");
            }
        } else {
            throw new EmptyFieldException("Job Id not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method Deletes the job using id
     *
     * @param jobId
     * @return
     */
    @Override
    public GeneralResponse deleteJob(long jobId) {
        logger.info("Inside deleteJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job Not Deleted");
        if (jobId > 0L) {
            if (jobRepository.existsById(jobId)) {
                jobRepository.deleteById(jobId);
                response.setStatus("Success");
                response.setMessage("Job Deleted");
            }
        } else {
            throw new EmptyFieldException("Job Id Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method adds category based on experience
     *
     * @param job
     */
    private void assignCategoryBasedOnExperience(Job job) {
        if (job.getMinExperience() != null && job.getMaxExperience() != null) {
            int minExperience = -2;
            int maxExperience = -2;
            try {

                minExperience = Integer.parseInt(job.getMinExperience());
                maxExperience = Integer.parseInt(job.getMaxExperience());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            if (minExperience == 0) {
                if (maxExperience == 0) {
                    Category category = categoryRepository.findByName("Internship");
                    job.getCategories().add(category);
                } else if (maxExperience == 1) {
                    Category category = categoryRepository.findByName("Fresher");
                    job.getCategories().add(category);
                } else if (maxExperience > 1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                } else if (maxExperience == -1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                    category = categoryRepository.findByName("Fresher");
                    job.getCategories().add(category);
                }
            } else if (minExperience == 1) {
                if (maxExperience == 1) {
                    Category category = categoryRepository.findByName("Fresher");
                    job.getCategories().add(category);
                } else if (maxExperience > 1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                } else if (maxExperience == -1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                }
            } else if (minExperience > 1) {
                if (minExperience <= maxExperience && maxExperience > 1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                } else if (maxExperience == -1) {
                    Category category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                }
            } else if (minExperience == -1) {
                if (maxExperience == 0) {
                    Category category = categoryRepository.findByName("Internship");
                    job.getCategories().add(category);
                } else if (maxExperience == 1) {
                    Category category = categoryRepository.findByName("Fresher");
                    job.getCategories().add(category);
                } else if (maxExperience > 1) {
                    Category category = categoryRepository.findByName("Fresher");
                    job.getCategories().add(category);
                    category = categoryRepository.findByName("Experience");
                    job.getCategories().add(category);
                } else if (maxExperience == -1) {
                    Category category = categoryRepository.findByName("Internship");
                    job.getCategories().add(category);
                }
            }
        }
    }

    /**
     * This method checks for apply link uniqueness
     *
     * @param applyLink
     */
    private void checkJobApplyLinkUniqueness(String applyLink) {
        if (jobRepository.existsByApplyLink(applyLink)) {
            throw new ResourceAlreadyExistsException("Job With Apply Link Already Exists!");
        }
    }

    /**
     * This method compares old job data with new data
     *
     * @param actualJob
     * @param job
     */
    private void compareJobDate(Job actualJob, Job job) {
        if (job.getProfileName() != null && !job.getProfileName().isEmpty()) {
            job.setProfileName(job.getProfileName().trim().toLowerCase());
            if (!actualJob.getProfileName().equalsIgnoreCase(job.getProfileName())) {
                actualJob.setProfileName(job.getProfileName());
            }
        }

        if (job.getNoOfOpening() > 0) {
            if (actualJob.getNoOfOpening() != job.getNoOfOpening()) {
                actualJob.setNoOfOpening(job.getNoOfOpening());
            }
        }

        if (job.getMinExperience() != null && !job.getMinExperience().isEmpty()) {
            job.setMinExperience(job.getMinExperience().trim());
            if (!actualJob.getMinExperience().equalsIgnoreCase(job.getMinExperience())) {
                actualJob.setMinExperience(job.getMinExperience());
            }
        }

        if (job.getMaxExperience() != null && !job.getMaxExperience().isEmpty()) {
            job.setMaxExperience(job.getMaxExperience().trim());
            if (!actualJob.getMaxExperience().equalsIgnoreCase(job.getMaxExperience())) {
                actualJob.setMaxExperience(job.getMaxExperience());
            }
        }

        if (job.getMinSalary() != null && !job.getMinSalary().isEmpty()) {
            job.setMinSalary(job.getMinSalary().trim());
            if (!actualJob.getMinSalary().equalsIgnoreCase(job.getMinSalary())) {
                actualJob.setMinSalary(job.getMinSalary());
            }
        }

        if (job.getMaxSalary() != null && !job.getMaxSalary().isEmpty()) {
            job.setMaxSalary(job.getMaxSalary().trim());
            if (!actualJob.getMaxSalary().equalsIgnoreCase(job.getMaxSalary())) {
                actualJob.setMaxSalary(job.getMaxSalary());
            }
        }

        if (job.getApplicationMode() != null && !job.getApplicationMode().isEmpty()) {
            job.setApplicationMode(job.getApplicationMode().trim());
            if (actualJob.getApplicationMode() != null && !actualJob.getApplicationMode().equalsIgnoreCase(job.getApplicationMode())) {
                actualJob.setApplicationMode(job.getApplicationMode());
            }
        }

        if (job.getApplyLink() != null && !job.getApplyLink().isEmpty()) {
            job.setApplyLink(job.getApplyLink().trim());
            checkJobApplyLinkUniqueness(job.getApplyLink());
            if (!actualJob.getApplyLink().equalsIgnoreCase(job.getApplyLink())) {
                actualJob.setApplyLink(job.getApplyLink());
            }
        }
    }

    /**
     * This method check for all the mandatory present in the job payload
     *
     * @param job
     */
    private void checkJobMandatoryData(Job job) {
        if (job != null) {
            if (job.getProfileName() == null || job.getProfileName().isEmpty()) {
                throw new EmptyFieldException("Profile Name is Not Present In Payload!");
            }
            job.setProfileName(job.getProfileName().trim());

            if (job.getApplyLink() == null || job.getApplyLink().isEmpty()) {
                throw new EmptyFieldException("Apply Link is Not Present In Payload!");
            }
            job.setApplyLink(job.getApplyLink().trim());
        } else {
            throw new EmptyFieldException("Job Data Not Present in Payload!");
        }
    }
}
