package in.getdreamjob.service;

import in.getdreamjob.model.Job;

import java.util.List;
import java.util.Set;

public interface HomeService {
    Set<Job> getAllJobsByLocations(String locationName);

    Set<Job> getAllJobsByQualifications(String qualificationName);

    List<String> getAllDistinctLocationNames();

    List<String> getAllDistinctQualificationNames();

    List<String> getAllJobTypes();

    Set<Job> getAllJobsByJobType(String typeName);
}
