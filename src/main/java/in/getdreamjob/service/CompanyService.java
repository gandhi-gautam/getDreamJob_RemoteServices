package in.getdreamjob.service;

import in.getdreamjob.model.Company;
import in.getdreamjob.model.GeneralResponse;

public interface CompanyService {
    public GeneralResponse createNewCompany(Company company);

    public GeneralResponse updateCompany(Company company);

    public GeneralResponse getAllCompanies();

    public GeneralResponse getCompany(long companyId);

    public GeneralResponse deleteCompany(long companyId);
}
