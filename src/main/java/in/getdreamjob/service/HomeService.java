package in.getdreamjob.service;

import in.getdreamjob.model.Job;

import java.util.Set;

public interface HomeService {
    Set<Job> getAllJobsByLocations(String locationName);

    Set<Job> getAllJobsByQualifications(String qualificationName);
}
