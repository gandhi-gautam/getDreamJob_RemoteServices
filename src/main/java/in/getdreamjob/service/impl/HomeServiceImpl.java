package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.enums.Category;
import in.getdreamjob.model.enums.Locations;
import in.getdreamjob.model.enums.Qualifications;
import in.getdreamjob.repository.CategoryRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.LocationRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.HomeService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static in.getdreamjob.service.impl.JobServiceImpl.PAGE_SIZE;

@Service
public class HomeServiceImpl implements HomeService {
    private static final Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);
    private final ResponseUtil responseUtil;
    private final JobRepository jobRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final QualificationRepository qualificationRepository;
    private Category category;
    private Locations locations;
    private Qualifications qualifications;

    @Autowired
    public HomeServiceImpl(ResponseUtil responseUtil, JobRepository jobRepository, CategoryRepository categoryRepository, LocationRepository locationRepository, QualificationRepository qualificationRepository) {
        this.responseUtil = responseUtil;
        this.jobRepository = jobRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.qualificationRepository = qualificationRepository;
    }

    /**
     * This method fetch all the latest jobs
     *
     * @param noOfJobs
     * @return
     */
    @Override
    public GeneralResponse getAllLatestJobs(int noOfJobs) {
        logger.info("Inside getAllLatestJobs service method");
        GeneralResponse response = responseUtil.createResponseObject("No Latest Job fetched");
        if (noOfJobs <= 0) {
            noOfJobs = 4;
        }
        List<Job> jobs = jobRepository.findTopNByOrderByCreatedOnDesc(noOfJobs);
        if (jobs != null && jobs.size() > 0) {
            response.setMessage("Latest Job Found!");
            response.setDetail(jobs);
            response.setStatus("Success");
        }
        return response;
    }

    /**
     * This method returns all the jobs by category name
     *
     * @param categoryName
     * @param pageNo
     * @return
     */
    @Override
    public GeneralResponse getJobByCategory(String categoryName, int pageNo) {
        logger.info("Inside getJobByCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("No Jobs Found!");
        if (categoryName != null && !categoryName.isEmpty()) {
            if (categoryRepository.existsByName(categoryName)) {
                PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
                Page<Job> jobs = jobRepository.findByCategories_NameAndIsDisableFalse(categoryName, request);
                if (jobs != null && jobs.getSize() > 0) {
                    response.setStatus("Success");
                    response.setMessage("Jobs Found!");
                    response.setDetail(jobs);
                }
            } else {
                throw new ResourceNotFoundException("Category With Name " + categoryName + " Not found!");
            }
        } else {
            throw new EmptyFieldException("Category Name Not Present in the Payload!");
        }
        return response;
    }

    /**
     * Ths method returns all the jobs by location name
     *
     * @param locationName
     * @param pageNo
     * @return
     */
    @Override
    public GeneralResponse getJobByLocation(String locationName, int pageNo) {
        logger.info("Inside getJobByLocation service method");
        GeneralResponse response = responseUtil.createResponseObject("No Jobs Found!");
        if (locationName != null && !locationName.isEmpty()) {
            if (locationRepository.existsByName(locationName)) {
                PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
                Page<Job> jobs = jobRepository.findByLocations_Name(locationName, request);
                if (jobs != null && jobs.getSize() > 0) {
                    response.setStatus("Success");
                    response.setMessage("Jobs Found!");
                    response.setDetail(jobs);
                }
            } else {
                throw new ResourceNotFoundException("Location With Name " + locationName + " Not found!");
            }
        } else {
            throw new EmptyFieldException("Location Name Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method returns all the jobs by qualification name
     *
     * @param qualificationName
     * @param pageNo
     * @return
     */
    @Override
    public GeneralResponse getJobByQualification(String qualificationName, int pageNo) {
        logger.info("Inside getJobByQualification service method");
        GeneralResponse response = responseUtil.createResponseObject("No Jobs Found!");
        if (qualificationName != null && !qualificationName.isEmpty()) {
            if (qualificationRepository.existsByName(qualificationName)) {
                PageRequest request = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdOn");
                Page<Job> jobs = jobRepository.findByQualifications_Name(qualificationName, request);
                if (jobs != null && jobs.getSize() > 0) {
                    response.setStatus("Success");
                    response.setMessage("Jobs Found!");
                    response.setDetail(jobs);
                }
            } else {
                throw new ResourceNotFoundException("Qualification With Name " + qualificationName + " Not found!");
            }
        } else {
            throw new EmptyFieldException("Qualification Name Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method returns all the distinct categories
     *
     * @return
     */
    @Override
    public GeneralResponse getAllDistinctCategories() {
        logger.info("Inside getAllDistinctCategories service method");
        GeneralResponse response = responseUtil.createResponseObject("No Category Found!");
        Category[] categories = Category.values();
        if (categories != null && categories.length > 0) {
            response.setDetail(categories);
            response.setMessage("Categories Found!");
            response.setStatus("Success");
        }
        return response;
    }

    /**
     * This method returns all the distinct locations
     *
     * @return
     */
    @Override
    public GeneralResponse getAllDistinctLocations() {
        logger.info("Inside getAllDistinctLocations service method");
        GeneralResponse response = responseUtil.createResponseObject("No Location Found!");
        Locations[] locationsList = Locations.values();
        if (locationsList != null && locationsList.length > 0) {
            response.setDetail(locationsList);
            response.setMessage("Locations Found!");
            response.setStatus("Success");
        }
        return response;
    }

    /**
     * This method returns all the distinct qualifications
     *
     * @return
     */
    @Override
    public GeneralResponse getAllDistinctQualifications() {
        logger.info("Inside getAllDistinctQualifications service method");
        GeneralResponse response = responseUtil.createResponseObject("No Qualification Found!");
        Qualifications[] qualificationsList = Qualifications.values();
        if (qualificationsList != null && qualificationsList.length > 0) {
            response.setDetail(qualificationsList);
            response.setMessage("Qualification Found!");
            response.setStatus("Success");
        }
        return response;
    }
}
