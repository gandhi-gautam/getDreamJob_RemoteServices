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
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<?> createNewLocation(@RequestBody Location location) {
        return new ResponseEntity<>(locationService.createNewLocation(location), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        return new ResponseEntity<>(locationService.updateLocation(location), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/{locationName}/{locationId}")
    public ResponseEntity<?> getLocationById(@PathVariable String locationName, @PathVariable long locationId) {
        return new ResponseEntity<>(locationService.getLocation(locationId), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}/{locationId}")
    public ResponseEntity<?> deleteLocationFromJob(@PathVariable long jobId, @PathVariable long locationId) {
        return new ResponseEntity<>(locationService.deleteLocationFromAJob(jobId, locationId), HttpStatus.OK);
    }

    @PostMapping("/{jobId}/{locationId}")
    public ResponseEntity<?> connectLocationFromJob(@PathVariable long jobId, @PathVariable long locationId) {
        return new ResponseEntity<>(locationService.connectLocationFromJob(jobId, locationId), HttpStatus.OK);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<?> deleteLocationById(@PathVariable long locationId) {
        return new ResponseEntity<>(locationService.deleteLocation(locationId), HttpStatus.OK);
    }
}
