package in.getdreamjob.service.impl;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private Set<Job> addCompanyDetails(Set<Job> jobs) {
        for (Job job : jobs) {
            if (job.getCompany() != null) {
                jobService.addCompanyDetails(job);
            }
        }
        return jobs;
    }
}
