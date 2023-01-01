package in.getdreamjob.service;

import in.getdreamjob.model.Company;

import java.util.List;

public interface CompanyService {
    public Company createNewCompany(Company company);

    public Company updateCompany(long companyId, Company company);

    public List<Company> getAllCompanies();

    public Company getCompany(long companyId);
}
