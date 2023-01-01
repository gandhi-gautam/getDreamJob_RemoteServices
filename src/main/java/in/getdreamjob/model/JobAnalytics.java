package in.getdreamjob.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class JobAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long noOfClicks;

    @OneToOne
    private Job job;
}
