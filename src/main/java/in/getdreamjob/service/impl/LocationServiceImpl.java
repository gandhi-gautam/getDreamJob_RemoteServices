package in.getdreamjob.service.impl;

import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            location = checkUniqueLocation(location);
            job.getLocations().add(location);
            location.getJobs().add(job);
            job = jobRepository.save(job);
            jobService.addCompanyDetails(job);
            return job;
        }
        throw new ResourceNotFoundException("Job with Id: " + jobId + " Not Found");
    }

    @Override
    public Location updateLocation(long jobId, long locationId, Location location) {
        Location tempLocation = locationRepository.findByName(location.getName());
        if (tempLocation == null) {
            Optional<Location> optionalLocation = locationRepository.findById(locationId);
            if (optionalLocation.isPresent()) {
                Location actualLocation = optionalLocation.get();
                validateLocationData(actualLocation, location);
                return locationRepository.save(actualLocation);
            }
            throw new ResourceNotFoundException("Location with Id: " + locationId + " not Found");
        }
        throw new ResourceAlreadyExistsException("Location with name: " + location.getName() + " Already exists");
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location getLocation(long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isPresent()) {
            return locationOptional.get();
        }
        throw new ResourceNotFoundException("Location with id: " + locationId + " Not Found");
    }

    private void validateLocationData(Location actualLocation, Location location) {
        if (location.getName() != null && !location.getName().isEmpty()) {
            actualLocation.setName(location.getName());
        }
    }


    private Location checkUniqueLocation(Location location) {
        Location tempLocation = locationRepository.findByName(location.getName());
        if (tempLocation != null) {
            return tempLocation;
        }
        return location;
    }
}
