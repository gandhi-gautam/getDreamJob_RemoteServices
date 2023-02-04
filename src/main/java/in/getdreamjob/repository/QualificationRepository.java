package in.getdreamjob.repository;

import in.getdreamjob.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    Qualification findByName(String name);

    @Query("SELECT DISTINCT q.name from Qualification q")
    List<String> findDistinctQualificationName();
}
