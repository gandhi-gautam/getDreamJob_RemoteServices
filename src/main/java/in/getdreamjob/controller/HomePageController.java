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

    @GetMapping("/location/{locationName}")
    public ResponseEntity<?> getAllJobsByLocationName(@PathVariable String locationName) {
        return new ResponseEntity<>(homeService.getAllJobsByLocations(locationName), HttpStatus.OK);
    }

    @GetMapping("/qualification/{qualificationName}")
    public ResponseEntity<?> getAllJobsByQualification(@PathVariable String qualificationName) {
        return new ResponseEntity<>(homeService.getAllJobsByQualifications(qualificationName), HttpStatus.OK);
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

    @GetMapping("/jobType/{typeName}")
    public ResponseEntity<?> getAllJobsByJobType(@PathVariable String typeName){
        return new ResponseEntity<>(homeService.getAllJobsByJobType(typeName), HttpStatus.OK);
    }
}
