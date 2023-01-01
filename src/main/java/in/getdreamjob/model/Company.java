package in.getdreamjob.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String officialWebsite;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private Set<Job> jobs = new HashSet<>();
}
