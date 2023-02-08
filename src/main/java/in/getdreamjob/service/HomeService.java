package in.getdreamjob.service;

import in.getdreamjob.model.Job;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HomeService {
    Page<Job> getAllJobsByLocations(String locationName, int pageNo);

    Page<Job> getAllJobsByQualifications(String qualificationName, int pageNo);

    List<String> getAllDistinctLocationNames();

    List<String> getAllDistinctQualificationNames();

    List<String> getAllJobTypes();

    Page<Job> getAllJobsByJobType(String typeName, int pageNo);
}
