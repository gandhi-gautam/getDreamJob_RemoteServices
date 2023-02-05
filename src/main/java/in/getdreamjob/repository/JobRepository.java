package in.getdreamjob.repository;

import in.getdreamjob.model.Job;
import in.getdreamjob.model.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Set<Job> findByJobType(JobType jobType);
}
