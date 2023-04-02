package in.getdreamjob.service.impl;

import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.QualificationService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Qualification tempQualification = qualificationRepository.findByName(qualification.getName().toLowerCase());
        if (tempQualification == null) {
            Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
            if (optionalQualification.isPresent()) {
                Qualification actualQualification = optionalQualification.get();
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

    @Override
    public Boolean deleteQualificationFromAJob(long jobId, long qualificationId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
            if (optionalQualification.isPresent()) {
                Set<Qualification> qualifications = jobOptional.get().getQualifications();
                qualifications.remove(optionalQualification.get());
                jobRepository.save(jobOptional.get());
                return true;
            }
            throw new ResourceNotFoundException("Qualification with Id: " + qualificationId + " Not Found!");
        }
        throw new ResourceNotFoundException("Job with Id: " + jobId + " Not Found!");
    }

    @Override
    public Object deleteQualification(long qualificationId) {
        Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
        if (optionalQualification.isPresent()) {
            Qualification qualification = optionalQualification.get();
            Set<Job> jobs = qualification.getJobs();
            for (Job job : jobs) {
                job.getQualifications().remove(qualification);
            }
            qualification.setJobs(new HashSet<>());
            qualificationRepository.delete(qualification);
        } else {
            throw new ResourceNotFoundException("Qualification with Id: " + qualificationId + " Not Found!");
        }
        return null;
    }

    private void validateQualificationData(Qualification actualQualification, Qualification qualification) {
        if (qualification.getName() != null && !qualification.getName().isEmpty()) {
            actualQualification.setName(qualification.getName().toLowerCase());
        }
    }

    private Qualification checkUniqueQualification(Qualification qualification) {
        qualification.setName(qualification.getName().toLowerCase());
        Qualification tempQualification = qualificationRepository.findByName(qualification.getName());
        if (tempQualification != null) {
            return tempQualification;
        }
        return qualification;
    }
}
