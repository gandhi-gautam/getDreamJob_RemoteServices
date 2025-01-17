package in.getdreamjob.controller;

import in.getdreamjob.model.Location;
import in.getdreamjob.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {
    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<?> createNewLocation(@PathVariable long jobId, @RequestBody Location location) {
        return new ResponseEntity<>(locationService.createNewLocation(jobId, location), HttpStatus.CREATED);
    }

    @PutMapping("/{jobId}/{locationId}")
    public ResponseEntity<?> updateLocation(@PathVariable long jobId, @PathVariable long locationId, @RequestBody Location location) {
        return new ResponseEntity<>(locationService.updateLocation(jobId, locationId, location), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<?> getLocationById(@PathVariable long locationId) {
        return new ResponseEntity<>(locationService.getLocation(locationId), HttpStatus.OK);
    }

    @PostMapping("/{jobId}/{locationId}")
    public ResponseEntity<?> deleteLocationFromJob(@PathVariable long jobId, @PathVariable long locationId) {
        return new ResponseEntity<>(locationService.deleteLocationFromAJob(jobId, locationId), HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<?> deleteLocationById(@PathVariable long locationId) {
        return new ResponseEntity<>(locationService.deleteLocation(locationId), HttpStatus.OK);
    }
}
