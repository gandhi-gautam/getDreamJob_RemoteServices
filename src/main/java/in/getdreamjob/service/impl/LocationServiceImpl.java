package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


    /**
     * This method creates new location, it needs job id that is mandatory. location saved is linked with the particular
     * job.
     *
     * @param jobId
     * @param location
     * @return
     */
    @Override
    public Job createNewLocation(long jobId, Location location) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            if (location.getName() != null) {
                location = checkUniqueLocation(location);
                job.getLocations().add(location);
                location.getJobs().add(job);
                job = jobRepository.save(job);
                jobService.addCompanyDetails(job);
                return job;
            }
            throw new EmptyFieldException("Location name not provided in payload");
        }
        throw new ResourceNotFoundException("Job with Id: " + jobId + " Not Found");
    }

    @Override
    public Location updateLocation(long jobId, long locationId, Location location) {
        Location tempLocation = locationRepository.findByName(location.getName().toLowerCase());
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

    /**
     * This method delete the location from the job
     *
     * @param jobId
     * @param locationId
     * @return
     */
    @Override
    public Boolean deleteLocationFromAJob(long jobId, long locationId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Optional<Location> optionalLocation = locationRepository.findById(locationId);
            if (optionalLocation.isPresent()) {
                Set<Location> locations = jobOptional.get().getLocations();
                locations.remove(optionalLocation.get());
                jobRepository.save(jobOptional.get());
                return true;
            }
            throw new ResourceNotFoundException("Location with Id: " + locationId + " Not Found!");
        }
        throw new ResourceNotFoundException("Job with Id: " + jobId + " Not Found!");
    }

    @Override
    public Object deleteLocation(long locationId) {
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();
            Set<Job> jobs = location.getJobs();
            for (Job job : jobs) {
                job.getLocations().remove(location);
            }
            location.setJobs(new HashSet<>());
            locationRepository.delete(location);
        } else {
            throw new ResourceNotFoundException("Location with Id: " + locationId + " Not Found!");
        }
        return null;
    }

    private void validateLocationData(Location actualLocation, Location location) {
        if (location.getName() != null && !location.getName().isEmpty()) {
            actualLocation.setName(location.getName().toLowerCase());
        }
    }


    /**
     * This
     * This method checks if the location name is already exists in the database if yes then return that location
     * if not then return new location that will be used to saved in the database
     *
     * @param location
     * @return
     */
    private Location checkUniqueLocation(Location location) {
        location.setName(location.getName().toLowerCase());
        Location tempLocation = locationRepository.findByName(location.getName());
        if (tempLocation != null) {
            return tempLocation;
        }
        return location;
    }
}
