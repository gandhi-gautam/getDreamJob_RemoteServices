package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Category;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.repository.CategoryRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.service.CategoryService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final JobRepository jobRepository;
    private final ResponseUtil responseUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, JobRepository jobRepository, ResponseUtil responseUtil) {
        this.categoryRepository = categoryRepository;
        this.jobRepository = jobRepository;
        this.responseUtil = responseUtil;
    }

    /**
     * This method saves a new Category
     *
     * @param category
     * @return
     */
    @Override
    public GeneralResponse createNewCategory(Category category) {
        logger.info("Inside createNewCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("Category Not Created!");
        validateCategoryMandatoryData(category);
        checkCategoryNameUniqueness(category.getName());
        category = categoryRepository.save(category);
        response.setMessage("Category Created");
        response.setDetail(category);
        response.setStatus("Success");
        return response;
    }

    /**
     * This method updates category Data
     *
     * @param category
     * @return
     */
    @Override
    public GeneralResponse updateCategory(Category category) {
        logger.info("Inside updateCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("Category Not Updated");
        if (category != null) {
            if (category.getId() > 0L) {
                Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
                if (optionalCategory.isEmpty()) {
                    throw new ResourceNotFoundException("Category with Id: " + category.getId() + " Not Found!");
                }
                Category actualCategory = optionalCategory.get();
                compareCategoryData(actualCategory, category);
                category = categoryRepository.save(category);
                response.setStatus("Success");
                response.setMessage("Category Updated");
                response.setDetail(category);
            } else {
                throw new EmptyFieldException("Category Id Not Present in the PayLoad!");
            }
        } else {
            throw new EmptyFieldException("Category Data Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method Fetches all the Category from Database
     *
     * @return
     */
    @Override
    public GeneralResponse getAllCategory() {
        logger.info("Inside getAllCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("No Category Found!");
        List<Category> categories = categoryRepository.findAll();
        response.setDetail(categories);
        response.setStatus("Success");
        response.setMessage("Categories Found");
        return response;
    }

    /**
     * This method finds category using id
     *
     * @param categoryId
     * @return
     */
    @Override
    public GeneralResponse getCategory(long categoryId) {
        logger.info("Inside getCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("Category Not Found!");
        if (categoryId > 0L) {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalCategory.isEmpty()) {
                throw new ResourceNotFoundException("Category with Id: " + categoryId + " Not Found!");
            }
            response.setDetail(optionalCategory.get());
            response.setStatus("Success");
            response.setMessage("Category Found!");
        } else {
            throw new EmptyFieldException("Category Id Not Present In The Payload!");
        }
        return response;
    }

    /**
     * This method remove category from job
     *
     * @param jobId
     * @param categoryId
     * @return
     */
    @Override
    public GeneralResponse deleteCategoryFromAJob(long jobId, long categoryId) {
        logger.info("Inside deleteCategoryFromAJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Category Not Disconnected");
        if (jobId > 0L && categoryId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalJob.isEmpty() || optionalCategory.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobId + " Or Category with Id: "
                        + categoryId + " Not Found!");
            }
            Set<Category> categories = optionalJob.get().getCategories();
            categories.remove(optionalCategory.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setDetail(job);
            response.setMessage("Category disconnected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Category Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method deletes category using id
     *
     * @param categoryId
     * @return
     */
    @Override
    public GeneralResponse deleteCategory(long categoryId) {
        logger.info("Inside deleteCategory service method");
        GeneralResponse response = responseUtil.createResponseObject("Category not Deleted!");
        if (categoryId > 0L) {
            if (!categoryRepository.existsById(categoryId)) {
                throw new ResourceNotFoundException("Category with Id: " + categoryId + " Not Found!");
            }
            categoryRepository.deleteById(categoryId);
            response.setMessage("Category Deleted!");
            response.setStatus("Success");
        } else {
            throw new EmptyFieldException("Category Id Not Present In the Payload!");
        }
        return response;
    }

    /**
     * This method connect Category with Job
     *
     * @param jobId
     * @param categoryId
     * @return
     */
    @Override
    public GeneralResponse connectCategoryFromJob(long jobId, long categoryId) {
        logger.info("Inside connectCategoryFromJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job with Category Not Connected");
        if (jobId > 0L && categoryId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (optionalJob.isEmpty() || optionalCategory.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobId + " Or Category with Id: "
                        + categoryId + " Not Found!");
            }
            Set<Category> categories = optionalJob.get().getCategories();
            categories.add(optionalCategory.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setDetail(job);
            response.setMessage("Category Connected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Category Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method check for all the mandatory Data present in the payload
     *
     * @param category
     */
    private void validateCategoryMandatoryData(Category category) {
        if (category != null) {
            if (category.getName() == null || category.getName().isEmpty()) {
                throw new EmptyFieldException("Category Name Not Present in the Payload!");
            }
            category.setName(category.getName().trim().toLowerCase());
        } else {
            throw new EmptyFieldException("Category Not Present in the Payload!");
        }
    }

    /**
     * This method check for category name uniqueness
     *
     * @param categoryName
     */
    private void checkCategoryNameUniqueness(String categoryName) {
        if (categoryRepository.existsByName(categoryName.toLowerCase())) {
            throw new ResourceAlreadyExistsException("Category With Name: " + categoryName + " Already Exists");
        }
    }

    /**
     * This method compare fields of old Category with new Category data
     *
     * @param actualCategory
     * @param category
     */
    private void compareCategoryData(Category actualCategory, Category category) {
        if (category.getName() != null && !category.getName().isEmpty()) {
            category.setName(category.getName().trim().toLowerCase());
            checkCategoryNameUniqueness(category.getName());
            if (!actualCategory.getName().equalsIgnoreCase(category.getName())) {
                actualCategory.setName(category.getName());
            }
        }
    }
}
