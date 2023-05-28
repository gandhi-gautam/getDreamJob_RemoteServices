package in.getdreamjob.service.impl;

import in.getdreamjob.exception.EmptyFieldException;
import in.getdreamjob.exception.ResourceAlreadyExistsException;
import in.getdreamjob.exception.ResourceNotFoundException;
import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import in.getdreamjob.repository.JobRepository;
import in.getdreamjob.repository.QualificationRepository;
import in.getdreamjob.service.QualificationService;
import in.getdreamjob.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QualificationServiceImpl implements QualificationService {

    private final static Logger logger = LoggerFactory.getLogger(QualificationServiceImpl.class);
    private final QualificationRepository qualificationRepository;
    private final JobRepository jobRepository;
    private final JobServiceImpl jobService;
    private final ResponseUtil responseUtil;

    @Autowired
    public QualificationServiceImpl(QualificationRepository qualificationRepository, JobRepository jobRepository,
                                    JobServiceImpl jobService, ResponseUtil responseUtil) {
        this.qualificationRepository = qualificationRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.responseUtil = responseUtil;
    }

    /**
     * This method saves a new qualification
     *
     * @param qualification
     * @return
     */
    @Override
    public GeneralResponse createNewQualification(Qualification qualification) {
        logger.info("Inside createNewQualification service method");
        GeneralResponse response = responseUtil.createResponseObject("Qualification Not Created!");
        validateQualificationMandatoryData(qualification);
        checkQualificationNameUniqueness(qualification.getName());
        qualification = qualificationRepository.save(qualification);
        response.setMessage("Qualification Created");
        response.setData(qualification);
        response.setStatus("Success");
        return response;
    }

    /**
     * This method updates qualification Data
     *
     * @param qualification
     * @return
     */
    @Override
    public GeneralResponse updateQualification(Qualification qualification) {
        logger.info("Inside updateQualification service method");
        GeneralResponse response = responseUtil.createResponseObject("Qualification Not Updated");
        if (qualification != null) {
            if (qualification.getId() > 0L) {
                Optional<Qualification> optionalQualification = qualificationRepository.findById(qualification.getId());
                if (optionalQualification.isEmpty()) {
                    throw new ResourceNotFoundException("Qualification with Id: " + qualification.getId() + " Not Found!");
                }
                Qualification actualQualification = optionalQualification.get();
                compareQualificationData(actualQualification, qualification);
                qualification = qualificationRepository.save(qualification);
                response.setStatus("Success");
                response.setMessage("Qualification Updated");
                response.setData(qualification);
            } else {
                throw new EmptyFieldException("Qualification Id Not Present in the PayLoad!");
            }
        } else {
            throw new EmptyFieldException("Qualification Data Not Present in the Payload!");
        }
        return response;
    }

    /**
     * This method Fetches all the Qualifications from Database
     *
     * @return
     */
    @Override
    public GeneralResponse getAllQualifications() {
        logger.info("Inside getAllQualifications service method");
        GeneralResponse response = responseUtil.createResponseObject("No Qualification Found!");
        List<Qualification> qualifications = qualificationRepository.findAll();
        response.setData(qualifications);
        response.setStatus("Success");
        response.setMessage("Qualifications Found");
        return response;
    }

    /**
     * This method finds qualification using id
     *
     * @param qualificationId
     * @return
     */
    @Override
    public GeneralResponse getQualification(long qualificationId) {
        logger.info("Inside getQualification service method");
        GeneralResponse response = responseUtil.createResponseObject("Qualification Not Found!");

        if (qualificationId > 0L) {
            Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
            if (optionalQualification.isEmpty()) {
                throw new ResourceNotFoundException("Qualification with Id: " + qualificationId + " Not Found!");
            }
            response.setData(optionalQualification.get());
            response.setStatus("Success");
            response.setMessage("Qualification Found!");
        } else {
            throw new EmptyFieldException("Qualification Id Not Present In The Payload!");
        }
        return response;
    }

    /**
     * This method deletes qualification using id
     *
     * @param qualificationId
     * @return
     */
    @Override
    public GeneralResponse deleteQualification(long qualificationId) {
        logger.info("Inside deleteQualificationFromAJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Qualification not Deleted!");
        if (qualificationId > 0L) {
            if (!qualificationRepository.existsById(qualificationId)) {
                throw new ResourceNotFoundException("Qualification with Id: " + qualificationId + " Not Found!");
            }
            qualificationRepository.deleteById(qualificationId);
            response.setMessage("Qualification Deleted!");
            response.setStatus("Success");
        } else {
            throw new EmptyFieldException("Qualification Id Not Present In the Payload!");
        }
        return response;
    }

    /**
     * This method remove qualification from job
     *
     * @param jobid
     * @param qualificationId
     * @return
     */
    @Override
    public GeneralResponse deleteQualificationFromAJob(long jobid, long qualificationId) {
        logger.info("Inside deleteQualificationFromAJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Qualification Not Disconnected");
        if (jobid > 0L && qualificationId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobid);
            Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
            if (optionalJob.isEmpty() || optionalQualification.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobid + " Or Qualification with Id: "
                        + qualificationId + " Not Found!");
            }
            Set<Qualification> qualifications = optionalJob.get().getQualifications();
            qualifications.remove(optionalQualification.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setData(job);
            response.setMessage("Qualification disconnected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Qualification Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method connect Qualification with Job
     *
     * @param jobId
     * @param qualificationId
     * @return
     */
    @Override
    public GeneralResponse connectQualificationFromJob(long jobId, long qualificationId) {
        logger.info("Inside connectQualificationFromJob service method");
        GeneralResponse response = responseUtil.createResponseObject("Job with Qualification Not Disconnected");
        if (jobId > 0L && qualificationId > 0L) {
            Optional<Job> optionalJob = jobRepository.findById(jobId);
            Optional<Qualification> optionalQualification = qualificationRepository.findById(qualificationId);
            if (optionalJob.isEmpty() || optionalQualification.isEmpty()) {
                throw new ResourceNotFoundException("Job with Id: " + jobId + " Or Qualification with Id: "
                        + qualificationId + " Not Found!");
            }
            Set<Qualification> qualifications = optionalJob.get().getQualifications();
            qualifications.add(optionalQualification.get());
            Job job = jobRepository.save(optionalJob.get());
            response.setStatus("Success");
            response.setData(job);
            response.setMessage("Qualification Connected from Job");
        } else {
            throw new EmptyFieldException("Job Id Or Qualification Id Not Present In The Payload");
        }
        return response;
    }

    /**
     * This method check for all the mandatory Data present in the payload
     *
     * @param qualification
     */
    private void validateQualificationMandatoryData(Qualification qualification) {
        if (qualification != null) {
            if (qualification.getName() == null || qualification.getName().isEmpty()) {
                throw new EmptyFieldException("Qualification Name Not Present in the Payload!");
            }
            qualification.setName(qualification.getName().trim().toLowerCase());
        } else {
            throw new EmptyFieldException("Qualification Not Present in the Payload!");
        }
    }

    /**
     * This method check for qualification name uniqueness
     *
     * @param qualificationName
     */
    private void checkQualificationNameUniqueness(String qualificationName) {
        if (qualificationRepository.existsByName(qualificationName.toLowerCase())) {
            throw new ResourceAlreadyExistsException("Qualification With Name: " + qualificationName + " Already Exists");
        }
    }

    /**
     * This method compare fields of old qualification with new qualification data
     *
     * @param actualQualification
     * @param qualification
     */
    private void compareQualificationData(Qualification actualQualification, Qualification qualification) {
        if (qualification.getName() != null && !qualification.getName().isEmpty()) {
            qualification.setName(qualification.getName().trim().toLowerCase());
            checkQualificationNameUniqueness(qualification.getName());
            if (!actualQualification.getName().equalsIgnoreCase(qualification.getName())) {
                actualQualification.setName(qualification.getName());
            }
        }
    }
}