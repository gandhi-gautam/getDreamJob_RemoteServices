package in.getdreamjob.service;

import in.getdreamjob.model.GeneralResponse;
import in.getdreamjob.model.Job;
import in.getdreamjob.model.Qualification;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;

@Service
public interface QualificationService {
    public GeneralResponse createNewQualification(Qualification qualification);

    public GeneralResponse updateQualification(Qualification qualification);

    public GeneralResponse getAllQualifications();

    public GeneralResponse getQualification(long qualificationId);

    public GeneralResponse deleteQualificationFromAJob(long jobId, long qualificationId);

    public GeneralResponse deleteQualification(long qualificationId);

    public GeneralResponse connectQualificationFromJob(long jobId, long qualificationId);
}
