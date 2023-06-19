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
    private final HomeService homeService;

    @Autowired
    public HomePageController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/latestJob/{noOfJobs}")
    public ResponseEntity<?> getLatestJobs(@PathVariable int noOfJobs) {
        return new ResponseEntity<>(homeService.getAllLatestJobs(noOfJobs), HttpStatus.OK);
    }

    @GetMapping("/jobByCategory/{categoryName}/{pageNo}")
    public ResponseEntity<?> getJobByCategory(@PathVariable String categoryName, @PathVariable int pageNo) {
        return new ResponseEntity<>(homeService.getJobByCategory(categoryName, pageNo), HttpStatus.OK);
    }

    @GetMapping("/jobByLocation/{locationName}/{pageNo}")
    public ResponseEntity<?> getJobByLocation(@PathVariable String locationName, @PathVariable int pageNo) {
        return new ResponseEntity<>(homeService.getJobByLocation(locationName, pageNo), HttpStatus.OK);
    }

    @GetMapping("/jobByQualification/{qualificationName}/{pageNo}")
    public ResponseEntity<?> getJobByQualification(@PathVariable String qualificationName, @PathVariable int pageNo) {
        return new ResponseEntity<>(homeService.getJobByQualification(qualificationName, pageNo), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllDistinctCategories(){
        return new ResponseEntity<>(homeService.getAllDistinctCategories(), HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getAllDistinctLocations(){
        return new ResponseEntity<>(homeService.getAllDistinctLocations(), HttpStatus.OK);
    }

    @GetMapping("/qualifications")
    public ResponseEntity<?> getAllDistinctQualifications(){
        return new ResponseEntity<>(homeService.getAllDistinctQualifications(), HttpStatus.OK);
    }
}
