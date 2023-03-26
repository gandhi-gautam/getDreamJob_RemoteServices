package in.getdreamjob.service.impl;

import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.QualificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QualificationServiceImpl implements QualificationService {

    private QualificationRepository qualificationRepository;
    private JobRepository jobRepository;
    private JobServiceImpl jobService;

    public QualificationServiceImpl(QualificationRepository qualificationRepository, JobRepository jobRepository,
                                    JobServiceImpl jobService) {
        this.qualificationRepository = qualificationRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @Override
    public Job createNewQualification(long jobId, Qualification qualification) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            qualification = checkUniqueQualification(qualification);
            job.getQualifications().add(qualification);
            qualification.getJobs().add(job);
            job = jobRepository.save(job);
            jobService.addCompanyDetails(job);
            return job;
        }
        throw new ResourceNotFoundException("Job With id: " + jobId + " Not found");
    }

    @Override
    public Qualification updateQualification(long jobId, long qualificationId, Qualification qualification) {
        Qualification tempQualification = qualificationRepository.findByName(qualification.getName());
        if (tempQualification == null) {
            if (qualificationRepository.existsById(qualificationId)) {
                Qualification actualQualification = qualificationRepository.findById(qualificationId).get();
                validateQualificationData(actualQualification, qualification);
                return qualificationRepository.save(actualQualification);
            }
            throw new ResourceNotFoundException("Qualification with id: " + qualificationId + " Not Found");
        }
        throw new ResourceAlreadyExistsException("Qualification with name: " + qualification.getName() + " Already " +
                "exists");
    }

    @Override
    public List<Qualification> getAllQualifications() {
        return qualificationRepository.findAll();
    }

    @Override
    public Qualification getQualification(long qualificationId) {
        Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
        if (optionalQualification.isPresent()) {
            return optionalQualification.get();
        }
        throw new ResourceNotFoundException("Qualification with id: " + qualificationId + " Not Found");
    }

    private void validateQualificationData(Qualification actualQualification, Qualification qualification) {
        if (qualification.getName() != null && !qualification.getName().isEmpty()) {
            actualQualification.setName(qualification.getName());
        }
    }

    private Qualification checkUniqueQualification(Qualification qualification) {
        Qualification tempQualification = qualificationRepository.findByName(qualification.getName());
        if (tempQualification != null) {
            return tempQualification;
        }
        return qualification;
    }
}
