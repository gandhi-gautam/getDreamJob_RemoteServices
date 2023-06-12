package in.getdreamjob.controller;

import in.getdreamjob.model.Job;
import in.getdreamjob.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/job")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/{companyId}")
    public ResponseEntity<?> createNewJob(@PathVariable long companyId, @RequestBody Job job) throws ParseException {
        return new ResponseEntity<>(jobService.createNewJob(companyId, job), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateJob(@RequestBody Job job) {
        return new ResponseEntity<>(jobService.updateJob(job), HttpStatus.OK);
    }

    @GetMapping("/{pageNo}")
    public ResponseEntity<?> getAllJobs(@PathVariable int pageNo) {
        return new ResponseEntity<>(jobService.getAllJobs(pageNo), HttpStatus.OK);
    }

    @GetMapping("/getJob/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable long jobId) {
        return new ResponseEntity<>(jobService.getJob(jobId), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJobById(@PathVariable long jobId) {
        return new ResponseEntity<>(jobService.deleteJob(jobId), HttpStatus.OK);
    }
}
