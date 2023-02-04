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

    private QualificationService qualificationService;

    @Autowired
    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<?> createNewQualification(@PathVariable long jobId, @RequestBody Qualification qualification) {
        return new ResponseEntity<>(qualificationService.createNewQualification(jobId, qualification), HttpStatus.CREATED);
    }

    @PutMapping("/{qualificationId}")
    public ResponseEntity<?> updateQualification(@PathVariable long qualificationId, @RequestBody Qualification qualification) {
        return new ResponseEntity<>(qualificationService.updateQualification(qualificationId, qualification), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllQualifications() {
        return new ResponseEntity<>(qualificationService.getAllQualifications(), HttpStatus.OK);
    }

    @GetMapping("/{qualificationId}")
    public ResponseEntity<?> getQualificationById(@PathVariable long qualificationId) {
        return new ResponseEntity<>(qualificationService.getQualification(qualificationId), HttpStatus.OK);
    }
}
