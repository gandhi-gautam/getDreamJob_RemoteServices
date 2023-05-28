package in.getdreamjob.controller;

import in.getdreamjob.model.Qualification;
import in.getdreamjob.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qualification")
public class QualificationController {

    private final QualificationService qualificationService;

    @Autowired
    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping
    public ResponseEntity<?> createNewQualification(@RequestBody Qualification qualification) {
        return new ResponseEntity<>(qualificationService.createNewQualification(qualification), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateQualification(@RequestBody Qualification qualification) {
        return new ResponseEntity<>(qualificationService.updateQualification(qualification), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllQualifications() {
        return new ResponseEntity<>(qualificationService.getAllQualifications(), HttpStatus.OK);
    }

    @GetMapping("/{qualificationName}/{qualificationId}")
    public ResponseEntity<?> getQualificationById(@PathVariable String qualificationName, @PathVariable long qualificationId) {
        return new ResponseEntity<>(qualificationService.getQualification(qualificationId), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}/{qualificationId}")
    public ResponseEntity<?> deleteQualificationFromJob(@PathVariable long jobId, @PathVariable long qualificationId) {
        return new ResponseEntity<>(qualificationService.deleteQualificationFromAJob(jobId, qualificationId), HttpStatus.OK);
    }

    @PostMapping("/{jobId}/{qualificationId}")
    public ResponseEntity<?> connectQualificationFromJob(@PathVariable long jobId, @PathVariable long qualificationId) {
        return new ResponseEntity<>(qualificationService.connectQualificationFromJob(jobId, qualificationId), HttpStatus.OK);
    }

    @DeleteMapping("/{qualificationId}")
    public ResponseEntity<?> deleteQualificationById(@PathVariable long qualificationId) {
        return new ResponseEntity<>(qualificationService.deleteQualification(qualificationId), HttpStatus.OK);
    }


}
