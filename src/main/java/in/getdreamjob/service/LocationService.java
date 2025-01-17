package in.getdreamjob.service;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;

import java.util.List;

public interface LocationService {
    public Job createNewLocation(long jobId, Location location);

    public Location updateLocation(long jobId, long locationId, Location location);

    public List<Location> getAllLocations();

    public Location getLocation(long locationId);

    public Boolean deleteLocationFromAJob(long jobId, long locationId);

    Object deleteLocation(long locationId);
}
