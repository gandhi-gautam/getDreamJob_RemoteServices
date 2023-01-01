package in.getdreamjob.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String profileName;
    private int noOfOpening;
    private String batchEligible;
    private String minSalary;
    private String maxSalary;
    private String applicationMode;
    private Date lastApplyDate;
    private String applyLink;
    @Transient
    private long companyId;
    @Lob
    private String jobDescription;
    @Lob
    private String basicQualification;
    @Lob
    private String preferredQualification;
    @Lob
    private Byte[] image;
    @OneToOne(cascade = CascadeType.ALL)
    private JobAnalytics jobAnalytics;

    @JsonIgnore
    @ManyToOne
    private Company company;
    @ManyToMany
    @JoinTable(name = "job_location",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<Location> locations = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "job_qualification",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "qualification_id"))
    private Set<Qualification> qualifications = new HashSet<>();
}
