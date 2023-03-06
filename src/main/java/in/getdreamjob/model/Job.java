package in.getdreamjob.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.getdreamjob.model.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Temporal(TemporalType.DATE)
    private Date lastApplyDate;
    private String applyLink;

    @Temporal(TemporalType.DATE)
    private Date createdOn;
    @Transient
    private long companyId;
    @Transient
    private String companyName;
    @Transient
    private String companyOfficialWebsite;
    @Lob
    private String jobDescription;
    @Lob
    private String basicQualification;
    @Lob
    private String preferredQualification;
    @Lob
    private Byte[] image;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @OneToOne(cascade = CascadeType.ALL)
    private JobAnalytics jobAnalytics;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "job_locations",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "location_id")}
    )
    private Set<Location> locations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "job_qualifications",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "qualification_id")}
    )
    private Set<Qualification> qualifications = new HashSet<>();
}
