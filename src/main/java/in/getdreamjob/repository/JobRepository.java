package in.getdreamjob.repository;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

//    Page<Job> findByJobType(JobType jobType, PageRequest request);

    Page<Job> findByLocations_Name(String name, PageRequest request);

    Page<Job> findByQualifications_Name(String name, PageRequest request);

    boolean existsByApplyLink(String applyLink);
}
