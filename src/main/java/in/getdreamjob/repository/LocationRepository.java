package in.getdreamjob.repository;

import in.getdreamjob.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByName(String name);

    @Query("SELECT DISTINCT l.name from Location l")
    List<String> findDistinctLocationName();

}
