package in.getdreamjob.repository;

import in.getdreamjob.model.JobAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAnalyticsRepository extends JpaRepository<JobAnalytics, Long> {
}
