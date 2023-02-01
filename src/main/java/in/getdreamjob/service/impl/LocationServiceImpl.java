package in.getdreamjob.service.impl;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;
    private JobRepository jobRepository;

    private JobServiceImpl jobService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, JobRepository jobRepository
            , JobServiceImpl jobService) {
        this.locationRepository = locationRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }


    @Override
    public Job createNewLocation(long jobId, Location location) {
        if (jobRepository.existsById(jobId)) {
            Job job = jobRepository.findById(jobId).get();
            job.getLocations().add(location);
            location.getJobs().add(job);
            job = jobRepository.save(job);
            jobService.addCompanyDetails(job);
            return job;
        }
        return null;
    }

    @Override
    public Location updateLocation(long locationId, Location location) {
        if (locationRepository.existsById(locationId)) {
            Location actualLocation = locationRepository.findById(locationId).get();
            validateLocationData(actualLocation, location);
            return locationRepository.save(actualLocation);
        }
        return null;
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location getLocation(long locationId) {
        if (locationRepository.existsById(locationId)) {
            return locationRepository.findById(locationId).get();
        }
        return null;
    }

    private void validateLocationData(Location actualLocation, Location location) {
        if (location.getName() != null && !location.getName().isEmpty()) {
            actualLocation.setName(location.getName());
        }
    }
}
