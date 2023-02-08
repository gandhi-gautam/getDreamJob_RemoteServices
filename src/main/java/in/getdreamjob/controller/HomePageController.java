package in.getdreamjob.controller;

import in.getdreamjob.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomePageController {
    private HomeService homeService;

    @Autowired
    public HomePageController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/location/{locationName}/{pageNo}")
    public ResponseEntity<?> getAllJobsByLocationName(@PathVariable String locationName, @PathVariable int pageNo) {
        return new ResponseEntity<>(homeService.getAllJobsByLocations(locationName,pageNo), HttpStatus.OK);
    }

    @GetMapping("/qualification/{qualificationName}/{pageNo}")
    public ResponseEntity<?> getAllJobsByQualification(@PathVariable String qualificationName, @PathVariable int pageNo) {
        return new ResponseEntity<>(homeService.getAllJobsByQualifications(qualificationName, pageNo), HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getAllDistinctLocationNames() {
        return new ResponseEntity<>(homeService.getAllDistinctLocationNames(), HttpStatus.OK);
    }

    @GetMapping("/qualifications")
    public ResponseEntity<?> getAllDistinctQualificationNames() {
        return new ResponseEntity<>(homeService.getAllDistinctQualificationNames(), HttpStatus.OK);
    }

    @GetMapping("/jobTypes")
    public ResponseEntity<?> getAllDistinctJobTypeNames() {
        return new ResponseEntity<>(homeService.getAllJobTypes(), HttpStatus.OK);
    }

    @GetMapping("/jobType/{typeName}/{pageNo}")
    public ResponseEntity<?> getAllJobsByJobType(@PathVariable String typeName, @PathVariable int pageNo){
        return new ResponseEntity<>(homeService.getAllJobsByJobType(typeName, pageNo), HttpStatus.OK);
    }
}
