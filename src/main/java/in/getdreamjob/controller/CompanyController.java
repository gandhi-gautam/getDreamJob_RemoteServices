package in.getdreamjob.controller;

import in.getdreamjob.model.Company;
import in.getdreamjob.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/createCompany")
    public ResponseEntity<?> createNewCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.createNewCompany(company), HttpStatus.CREATED);
    }

    @PutMapping("/updateCompany/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable long companyId, @RequestBody Company company) {
        return new ResponseEntity<>(companyService.updateCompany(companyId, company), HttpStatus.OK);
    }

    @GetMapping("/getAllCompanies")
    public ResponseEntity<?> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/getCompany/{companyId}")
    public ResponseEntity<?> getCompany(@PathVariable long companyId) {
        return new ResponseEntity<>(companyService.getCompany(companyId), HttpStatus.OK);
    }

    @DeleteMapping("/company/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable long companyId) {
        return new ResponseEntity<>(companyService.deleteCompany(companyId), HttpStatus.OK);
    }
}
