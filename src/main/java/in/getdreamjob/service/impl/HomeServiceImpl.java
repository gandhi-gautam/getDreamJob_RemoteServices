package in.getdreamjob.service.impl;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.enums.JobType;
import in.getdreamjob.model.enums.Locations;
import in.getdreamjob.model.enums.Qualifications;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    public static final int PAGE_SIZE = 100;
    private LocationRepository locationRepository;
    private QualificationRepository qualificationRepository;

    private JobServiceImpl jobService;

    private JobRepository jobRepository;

    @Autowired
    public HomeServiceImpl(LocationRepository locationRepository, QualificationRepository qualificationRepository,
                           JobServiceImpl jobService, JobRepository jobRepository) {
        this.locationRepository = locationRepository;
        this.qualificationRepository = qualificationRepository;
        this.jobService = jobService;
        this.jobRepository = jobRepository;
    }

    private static PageRequest getPageRequest(int pageNo) {
        PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
        return request;
    }

    @Override
    public Page<Job> getAllJobsByLocations(String locationName, int pageNo) {
        PageRequest request = getPageRequest(pageNo);
        Page<Job> jobs = jobRepository.findByLocations_Name(locationName, request);
        return addCompanyDetails(jobs);
    }

    @Override
    public Page<Job> getAllJobsByQualifications(String qualificationName, int pageNo) {
        PageRequest request = getPageRequest(pageNo);
        Page<Job> jobs = jobRepository.findByQualifications_Name(qualificationName, request);
        return addCompanyDetails(jobs);
    }

    @Override
    public List<String> getAllJobTypes() {
        List<String> jobTypes = new ArrayList<>();
        JobType[] jobs = JobType.values();
        for (JobType job : jobs) {
            jobTypes.add(job.name());
        }
        return jobTypes;
    }

    @Override
    public Page<Job> getAllJobsByJobType(String typeName, int pageNo) {
        PageRequest request = getPageRequest(pageNo);
        JobType jobType = JobType.valueOf(typeName);
        Page<Job> jobs = jobRepository.findByJobType(jobType, request);
        return addCompanyDetails(jobs);
    }

    @Override
    public List<String> getAllDistinctLocationNames() {
        List<String> locations = new ArrayList<>();
        for (Locations job : Locations.values()) {
            locations.add(job.name());
        }
        return locations;
    }

    @Override
    public List<String> getAllDistinctQualificationNames() {
        List<String> qualifications = new ArrayList<>();
        for (Qualifications qualification : Qualifications.values()) {
            qualifications.add(qualification.getDisplayName());
        }
        return qualifications;
    }

    private Page<Job> addCompanyDetails(Page<Job> jobs) {
        for (Job job : jobs) {
            if (job.getCompany() != null) {
                jobService.addCompanyDetails(job);
            }
        }
        return jobs;
    }
}
