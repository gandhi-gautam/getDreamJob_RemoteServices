package in.getdreamjob.service.impl;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.model.enums.JobType;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class HomeServiceImpl implements HomeService {
    private LocationRepository locationRepository;
    private QualificationRepository qualificationRepository;

    private JobServiceImpl jobService;

    @Autowired
    public HomeServiceImpl(LocationRepository locationRepository, QualificationRepository qualificationRepository,
                           JobServiceImpl jobService) {
        this.locationRepository = locationRepository;
        this.qualificationRepository = qualificationRepository;
        this.jobService = jobService;
    }


    @Override
    public Set<Job> getAllJobsByLocations(String locationName) {
        Location location = locationRepository.findByName(locationName);
        if (location != null) {
            Set<Job> jobs = location.getJobs();
            return addCompanyDetails(jobs);
        }
        return null;
    }


    @Override
    public Set<Job> getAllJobsByQualifications(String qualificationName) {
        Qualification qualification = qualificationRepository.findByName(qualificationName);
        if (qualification != null) {
            Set<Job> jobs = qualification.getJobs();
            return addCompanyDetails(jobs);
        }
        return null;
    }

    @Override
    public List<String> getAllDistinctLocationNames() {
        return locationRepository.findDistinctLocationName();
    }

    @Override
    public List<String> getAllDistinctQualificationNames() {
        return qualificationRepository.findDistinctQualificationName();
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

    private Set<Job> addCompanyDetails(Set<Job> jobs) {
        for (Job job : jobs) {
            if (job.getCompany() != null) {
                jobService.addCompanyDetails(job);
            }
        }
        return jobs;
    }
}
