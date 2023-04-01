package in.getdreamjob.service;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QualificationService {
    public Job createNewQualification(long jobId, Qualification qualification);

    public Qualification updateQualification(long jobId, long qualificationId, Qualification qualification);

    public List<Qualification> getAllQualifications();

    public Qualification getQualification(long qualificationId);

    Boolean deleteQualificationFromAJob(long jobId, long qualificationId);
}
