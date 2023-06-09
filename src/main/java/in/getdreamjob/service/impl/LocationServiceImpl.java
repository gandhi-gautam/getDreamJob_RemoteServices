package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.service.LocationService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LocationServiceImpl implements LocationService {

    private final static Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    private final LocationRepository locationRepository;
    private final JobRepository jobRepository;
    private final ResponseUtil responseUtil;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, JobRepository jobRepository
            , ResponseUtil responseUtil) {
        this.locationRepository = locationRepository;
        this.jobRepository = jobRepository;
        this.responseUtil = responseUtil;
    }

    /**
     * This method saves a new Location
     *
     * @param location
     * @return
     */
    @Override
    public GeneralResponse createNewLocation(Location location) {
        logger.info("Inside createNewLocation service method");
        GeneralResponse response = responseUtil.createResponseObject("Location Not Created!");
        validateLocationMandatoryData(location);
        checkLocationNameUniqueness(location.getName());
        location = locationRepository.save(location);
        response.setMessage("Location Created");
        response.setData(location);
        response.setStatus("Success");
        return response;
    }

    /**
     * This method updates location Data
     *
     * @param location
     * @return
     */
    @Override
    public GeneralResponse updateLocation(Location location) {
        logger.info("Inside updateLocation service method");
        GeneralResponse response = responseUtil.createResponseObject("Location Not Updated");
        if (location != null) {
            if (location.getId() > 0L) {
                Optional<Location> optionalLocation = locationRepository.findById(location.getId());
                if (optionalLocation.isEmpty()) {
                    throw new ResourceNotFoundException("Location with Id: " + location.getId() + " Not Found!");
                }
                Location actualLocation = optionalLocation.get();
                compareLocationData(actualLocation, location);
                location = locationRepository.save(location);
                response.setStatus("Success");
                response.setMessage("Location Updated");
                response.setData(location);
            } else {
                throw new EmptyFieldException("Location Id Not Present in the PayLoad!");
            }
        } else {
            throw new EmptyFieldException("Location Data Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method Fetches all the Location from Database
     *
     * @return
     */
    @Override
    public GeneralResponse getAllLocations() {
        logger.info("Inside getAllLocations service method");
        GeneralResponse response = responseUtil.createResponseObject("No Location Found!");
        List<Location> locations = locationRepository.findAll();
        response.setData(locations);
        response.setStatus("Success");
        response.setMessage("Locations Found!");
        return response;
    }

    /**
     * This method finds location using id
     *
     * @param locationId
     * @return
     */
    @Override
    public GeneralResponse getLocation(long locationId) {
        logger.info("Inside getLocation service method");
        GeneralResponse response = responseUtil.createResponseObject("Location Not Found!");
        if (locationId > 0L) {
            Optional<Location> optionalLocation = locationRepository.findById(locationId);
            if (optionalLocation.isEmpty()) {
                throw new ResourceNotFoundException("Location with Id: " + locationId + " Not Found!");
            }
            response.setData(optionalLocation.get());
            response.setStatus("Success");
            response.setMessage("Location Found!");
        } else {
            throw new EmptyFieldException("Location Id Not Present In The Payload!");
        }
        return response;
    }

    /**
     * This method remove location from job
     *
     * @param jobId
     * @param locationId
     * @return
     */
    @Override
    public GeneralResponse deleteLocationFromAJob(long jobId, long locationId) {
        logger.info("Inside deleteLocationFromAJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Location Not Disconnected");
        if (jobId > 0L && locationId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            Optional<Location> optionalLocation = locationRepository.findById(locationId);
            if (optionalJob.isEmpty() || optionalLocation.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobId + " Or Location with Id: "
                        + locationId + " Not Found!");
            }
            Set<Location> locations = optionalJob.get().getLocations();
            locations.remove(optionalLocation.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setData(job);
            response.setMessage("Location disconnected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Location Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method connect Location with Job
     *
     * @param jobId
     * @param locationId
     * @return
     */
    @Override
    public GeneralResponse connectLocationFromJob(long jobId, long locationId) {
        logger.info("Inside connectLocationFromJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job with Location Not Connected");
        if (jobId > 0L && locationId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            Optional<Location> optionalLocation = locationRepository.findById(locationId);
            if (optionalJob.isEmpty() || optionalLocation.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobId + " Or Location with Id: "
                        + locationId + " Not Found!");
            }
            Set<Location> locations = optionalJob.get().getLocations();
            locations.add(optionalLocation.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setData(job);
            response.setMessage("Location Connected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Location Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method deletes location using id
     *
     * @param locationId
     * @return
     */
    @Override
    public GeneralResponse deleteLocation(long locationId) {
        logger.info("Inside deleteLocation service method");
        GeneralResponse response = responseUtil.createResponseObject("Location not Deleted!");
        if (locationId > 0L) {
            if (!locationRepository.existsById(locationId)) {
                throw new ResourceNotFoundException("Location with Id: " + locationId + " Not Found!");
            }
            locationRepository.deleteById(locationId);
            response.setMessage("Location Deleted!");
            response.setStatus("Success");
        } else {
            throw new EmptyFieldException("Location Id Not Present In the Payload!");
        }
        return response;
    }

    /**
     * This method check for all the mandatory Data present in the payload
     *
     * @param location
     */
    private void validateLocationMandatoryData(Location location) {
        if (location != null) {
            if (location.getName() == null || location.getName().isEmpty()) {
                throw new EmptyFieldException("Location Name Not Present in the Payload!");
            }
            location.setName(location.getName().trim().toLowerCase());
        } else {
            throw new EmptyFieldException("Location Not Present in the Payload!");
        }
    }

    /**
     * This method check for location name uniqueness
     *
     * @param locationName
     */
    private void checkLocationNameUniqueness(String locationName) {
        if (locationRepository.existsByName(locationName.toLowerCase())) {
            throw new ResourceAlreadyExistsException("Location With Name: " + locationName + " Already Exists");
        }
    }

    /**
     * This method compare fields of old location with new location data
     *
     * @param actualLocation
     * @param location
     */
    private void compareLocationData(Location actualLocation, Location location) {
        if (location.getName() != null && !location.getName().isEmpty()) {
            location.setName(location.getName().trim().toLowerCase());
            checkLocationNameUniqueness(location.getName());
            if (!actualLocation.getName().equalsIgnoreCase(location.getName())) {
                actualLocation.setName(location.getName());
            }
        }
    }
}
