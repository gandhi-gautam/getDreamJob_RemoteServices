package in.getdreamjob.repository;

import in.getdreamjob.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

//    Page<Job> findByJobType(JobType jobType, PageRequest request);

    Page<Job> findByLocations_Name(String name, PageRequest request);

    Page<Job> findByQualifications_Name(String name, PageRequest request);

    boolean existsByApplyLink(String applyLink);

    @Query(value = "SELECT * FROM job ORDER BY created_on DESC LIMIT ?1", nativeQuery = true)
    List<Job> findTopNByOrderByCreatedOnDesc(int n);

    Page<Job> findByCategories_Name(String categoryName, PageRequest request);
}
