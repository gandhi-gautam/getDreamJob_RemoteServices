package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Company;
import in.getdreamjob.repository.CompanyRepository;
import in.getdreamjob.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company createNewCompany(Company company) {
        boolean isValidated = validateData(company);
        if (isValidated) {
            Optional optional = companyRepository.findByName(company.getName());
            if (optional.isPresent()) {
                throw new ResourceAlreadyExistsException("Company Already exists in the Database with name: " +
                        company.getName());
            }
            return companyRepository.save(company);
        }
        return null;
    }

    @Override
    public Company updateCompany(long companyId, Company company) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company actualCompany = optionalCompany.get();
            validateCompanyData(actualCompany, company);
            return companyRepository.save(actualCompany);
        } else {
            throw new ResourceNotFoundException("Try to update company data, company data not exist in the database " +
                    "for id: " + companyId);
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompany(long companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        } else {
            throw new ResourceNotFoundException("Company not found in the database with Id: " + companyId);
        }
    }

    private void validateCompanyData(Company actualCompany, Company company) {
        if (company.getName() != null && !company.getName().isEmpty()) {
            Optional<Company> optional = companyRepository.findByName(company.getName());
            if (optional.isPresent() && optional.get().getId() != company.getId()) {
                throw new ResourceAlreadyExistsException("Cannot change name of the company, name " + company.getName()
                        + " Already exists");
            }
            actualCompany.setName(company.getName());
        }

        if (company.getOfficialWebsite() != null && !company.getOfficialWebsite().isEmpty()) {
            actualCompany.setOfficialWebsite(company.getOfficialWebsite());
        }
    }

    private boolean validateData(Company company) {
        if (company == null) {
            throw new EmptyFieldException("Company data not provided in the payload");
        }

        if (company.getName() == null) {
            throw new EmptyFieldException("Company Name not provided in payload");
        }

        if (company.getOfficialWebsite() == null) {
            throw new EmptyFieldException("Company official website not provided in payload");
        }

        return true;
    }
}
