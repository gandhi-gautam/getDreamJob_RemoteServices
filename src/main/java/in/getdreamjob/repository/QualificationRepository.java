package in.getdreamjob.repository;

import in.getdreamjob.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    Qualification findByName(String name);
}
