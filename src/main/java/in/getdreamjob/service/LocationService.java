package in.getdreamjob.service;

import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Location;

public interface LocationService {
    public GeneralResponse createNewLocation(Location location);

    public GeneralResponse updateLocation(Location location);

    public GeneralResponse getAllLocations();

    public GeneralResponse getLocation(long locationId);

    public GeneralResponse deleteLocationFromAJob(long jobId, long locationId);

    public GeneralResponse deleteLocation(long locationId);

    public GeneralResponse connectLocationFromJob(long jobId, long locationId);
}
