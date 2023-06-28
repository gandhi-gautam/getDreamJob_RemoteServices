package in.getdreamjob.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private int noOfOpening = 10;

    private String minExperience = "-1";

    private String maxExperience = "-1";

    private String minSalary = "4";

    private String maxSalary = "7";

    private String applicationMode = "Online";

    private LocalDateTime lastApplyDate;

    private String applyLink;

    private LocalDateTime createdOn;

    @Transient
    private long companyId;

    @Transient
    private String companyName;

    @Transient
    private String companyOfficialWebsite;

    @Transient
    private double companyRating;

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
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "job_qualifications",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "qualification_id")}
    )
    private Set<Qualification> qualifications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "job_categories",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Set<Category> categories = new HashSet<>();
}
