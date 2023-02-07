package in.getdreamjob.controller;

import in.getdreamjob.model.Job;
import in.getdreamjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1")
//@CrossOrigin(origins = "http://localhost:3000")
public class JobController {
    private JobService jobService;

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/createNewJob/{companyId}")
    public ResponseEntity<?> createNewJob(@PathVariable long companyId, @RequestBody Job job) throws ParseException {
        return new ResponseEntity<>(jobService.createNewJob(companyId, job), HttpStatus.CREATED);
    }

    @PutMapping("/updateJob/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable long jobId, @RequestBody Job job) {
        return new ResponseEntity<>(jobService.updateJob(jobId, job), HttpStatus.OK);
    }

    @GetMapping("/getAllJobs")
    public ResponseEntity<?> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/getJob/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable long jobId) {
        return new ResponseEntity<>(jobService.getJob(jobId), HttpStatus.OK);
    }

}
