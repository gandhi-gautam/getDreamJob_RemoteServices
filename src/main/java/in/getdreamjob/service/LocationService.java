package in.getdreamjob.service;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Location;

import java.util.List;

public interface LocationService {
    public Job createNewLocation(long jobId, Location location);

    public Location updateLocation(long locationId, Location location);

    public List<Location> getAllLocations();

    public Location getLocation(long locationId);
}
