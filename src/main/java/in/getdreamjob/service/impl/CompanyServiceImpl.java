package in.getdreamjob.service.impl;

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
        if (company != null) {
            return companyRepository.save(company);
        }
        return null;
    }

    @Override
    public Company updateCompany(long companyId, Company company) {
        Optional<Company> optionalCompany = null;
        if (companyRepository.existsById(companyId)) {
            optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isPresent()) {
                Company actualCompany = optionalCompany.get();
                validateCompanyData(actualCompany, company);
                return companyRepository.save(actualCompany);
            }
        }

        // if not exists then add exception case
        return null;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompany(long companyId) {
        if (companyRepository.existsById(companyId)) {
            Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isPresent()) {
                return optionalCompany.get();
            }
        }
        // create an exception situation where to raise exception when company not present
        return new Company();
    }

    private void validateCompanyData(Company actualCompany, Company company) {
        if (company.getName() != null && !company.getName().isEmpty()) {
            actualCompany.setName(company.getName());
        }

        if (company.getOfficialWebsite() != null && !company.getOfficialWebsite().isEmpty()) {
            actualCompany.setOfficialWebsite(company.getOfficialWebsite());
        }
    }
}
