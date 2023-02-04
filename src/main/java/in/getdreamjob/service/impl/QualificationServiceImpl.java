package in.getdreamjob.service.impl;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.QualificationService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (jobRepository.existsById(jobId)) {
            Job job = jobRepository.findById(jobId).get();
            qualification = checkUniqueQualification(qualification);
            job.getQualifications().add(qualification);
            qualification.getJobs().add(job);
            job = jobRepository.save(job);
            jobService.addCompanyDetails(job);
            return job;
        }
        return null;
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
        }
        return null;
    }

    @Override
    public List<Qualification> getAllQualifications() {
        return qualificationRepository.findAll();
    }

    @Override
    public Qualification getQualification(long qualificationId) {
        if (qualificationRepository.existsById(qualificationId)) {
            return qualificationRepository.findById(qualificationId).get();
        }
        return null;
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
