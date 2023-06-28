package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Company;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.repository.CompanyRepository;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.service.CompanyService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ResponseUtil responseUtil;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, JobRepository jobRepository, ResponseUtil responseUtil) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.responseUtil = responseUtil;
    }

    /**
     * This method saves a new Company
     *
     * @param company
     * @return
     */
    @Override
    public GeneralResponse createNewCompany(Company company) {
        logger.info("Inside createNewCompany service method");
        GeneralResponse response = responseUtil.createResponseObject("Company not Created!");
        validateCompanyMandatoryData(company);
        checkCompanyNameUniqueness(company.getName());
        checkCompanyOfficialWebsiteUniqueness(company.getOfficialWebsite());
        company = companyRepository.save(company);
        response.setMessage("Company Created");
        response.setDetail(company);
        response.setStatus("Success");
        return response;
    }

    /**
     * This method updates company date based on id
     *
     * @param company
     * @return
     */
    @Override
    public GeneralResponse updateCompany(Company company) {
        logger.info("Inside updateCompany service method");
        GeneralResponse response = responseUtil.createResponseObject("Company not Updated!");
        if (company != null) {
            if (company.getId() > 0L) {
                Optional<Company> optionalCompany = companyRepository.findById(company.getId());
                if (optionalCompany.isEmpty()) {
                    throw new ResourceNotFoundException("Company with Id: " + company.getId() + " Not Found!");
                }
                Company actualCompany = optionalCompany.get();
                compareCompanyData(actualCompany, company);
                actualCompany = companyRepository.save(actualCompany);
                response.setStatus("Success");
                response.setDetail(actualCompany);
                response.setMessage("Company Saved!");
            } else {
                throw new EmptyFieldException("Company Id Not Present in the PayLoad!");
            }
        } else {
            throw new EmptyFieldException("Company Data Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method fetches all the companies from the database
     *
     * @return
     */
    @Override
    public GeneralResponse getAllCompanies() {
        logger.info("Inside getAllCompanies service method");
        GeneralResponse response = responseUtil.createResponseObject("No Company Found!");
        List<Company> companies = companyRepository.findAll();
        response.setMessage("Company Found!");
        response.setStatus("Success");
        response.setDetail(companies);
        return response;
    }

    /**
     * This method finds company using id
     *
     * @param companyId
     * @return
     */
    @Override
    public GeneralResponse getCompany(long companyId) {
        logger.info("Inside getCompany service method");
        GeneralResponse response = responseUtil.createResponseObject("Company Not Found!");
        if (companyId > 0L) {
            Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isEmpty()) {
                throw new ResourceNotFoundException("Company with Id: " + companyId + " Not Found!");
            }
            response.setDetail(optionalCompany.get());
            response.setStatus("Success");
            response.setMessage("Company Found!");
        } else {
            throw new EmptyFieldException("Company Id Not Present In The Payload!");
        }
        return response;
    }

    /**
     * This method deletes company using id
     *
     * @param companyId
     * @return
     */
    @Override
    public GeneralResponse deleteCompany(long companyId) {
        logger.info("Inside deleteCompany service method");
        GeneralResponse response = responseUtil.createResponseObject("Company not Deleted!");
        if (companyId > 0L) {
            if (!companyRepository.existsById(companyId)) {
                throw new ResourceNotFoundException("Company with Id: " + companyId + " Not Found!");
            }
            companyRepository.deleteById(companyId);
            response.setMessage("Company Deleted!");
            response.setStatus("Success");
        } else {
            throw new EmptyFieldException("Company Id Not Present In the Payload!");
        }
        return response;
    }

    /**
     * This method check for all the mandatory Data present in the payload
     *
     * @param company
     */
    private void validateCompanyMandatoryData(Company company) {
        if (company != null) {
            if (company.getName() == null || company.getName().isEmpty()) {
                throw new EmptyFieldException("Company Name not Present in payload");
            }

            if (company.getOfficialWebsite() == null || company.getOfficialWebsite().isEmpty()) {
                throw new EmptyFieldException("Company official website not Present in payload");
            }

            if (company.getRating() <= 0) {
                throw new EmptyFieldException("Company Rating not Present in payload");
            }

            company.setName(company.getName().trim());
            company.setOfficialWebsite(company.getOfficialWebsite().trim());
        } else {
            throw new EmptyFieldException("Company data not provided in the payload");
        }
    }

    /**
     * This method check for company name uniqueness
     *
     * @param companyName
     */
    private void checkCompanyNameUniqueness(String companyName) {
        if (companyRepository.existsByName(companyName)) {
            throw new ResourceAlreadyExistsException("Company with Name: " + companyName + " Already Exists!");
        }
    }

    /**
     * This method check for company official website uniqueness
     *
     * @param officialWebsite
     */
    private void checkCompanyOfficialWebsiteUniqueness(String officialWebsite) {
        if (companyRepository.existsByOfficialWebsite(officialWebsite)) {
            throw new ResourceAlreadyExistsException("Company with Official Website: " + officialWebsite + " Already Exists!");
        }
    }

    /**
     * This method compare the old values with new values and take appropriate action
     *
     * @param actualCompany
     * @param location
     */
    private void compareCompanyData(Company actualCompany, Company location) {
        if (location.getName() != null && !location.getName().isEmpty()) {
            location.setName(location.getName().trim().toLowerCase());
            checkCompanyNameUniqueness(location.getName());
            if (!actualCompany.getName().equalsIgnoreCase(location.getName())) {
                actualCompany.setName(location.getName());
            }
        }

        if (location.getOfficialWebsite() != null && !location.getOfficialWebsite().isEmpty()) {
            location.setOfficialWebsite(location.getOfficialWebsite().trim().toLowerCase());
            checkCompanyOfficialWebsiteUniqueness(location.getOfficialWebsite());
            if (!actualCompany.getOfficialWebsite().equalsIgnoreCase(location.getOfficialWebsite())) {
                actualCompany.setOfficialWebsite(location.getOfficialWebsite());
            }
        }
    }
}
