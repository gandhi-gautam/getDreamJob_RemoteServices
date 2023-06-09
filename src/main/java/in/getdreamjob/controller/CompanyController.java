package in.getdreamjob.controller;

import in.getdreamjob.model.Company;
import in.getdreamjob.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
    private CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<?> createNewCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.createNewCompany(company), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCompany(@RequestBody Company company) {
        return new ResponseEntity<>(companyService.updateCompany(company), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping("/{companyName}/{companyId}")
    public ResponseEntity<?> getCompanyId(@PathVariable String companyName, @PathVariable long companyId) {
        return new ResponseEntity<>(companyService.getCompany(companyId), HttpStatus.OK);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompanyById(@PathVariable long companyId) {
        return new ResponseEntity<>(companyService.deleteCompany(companyId), HttpStatus.OK);
    }
}
