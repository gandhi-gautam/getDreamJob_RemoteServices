package in.getdreamjob.service;

import in.getdreamjob.model.GeneralResponse;

public interface HomeService {

    public GeneralResponse getAllLatestJobs(int noOfJobs);

    public GeneralResponse getJobByCategory(String categoryName, int pageNo);

    public GeneralResponse getJobByLocation(String locationName, int pageNo);

    public GeneralResponse getJobByQualification(String qualificationName, int pageNo);

    public GeneralResponse getAllDistinctCategories();
    public GeneralResponse getAllDistinctLocations();
    public GeneralResponse getAllDistinctQualifications();
}
