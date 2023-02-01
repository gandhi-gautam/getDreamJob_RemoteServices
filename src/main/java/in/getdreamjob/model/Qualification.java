package in.getdreamjob.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
//    @ManyToMany(mappedBy = "qualifications")
//    private Set<Job> jobs;
}
