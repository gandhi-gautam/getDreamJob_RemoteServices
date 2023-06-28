package in.getdreamjob.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralResponse {
    private Object detail;
    private String message;
    private String status;
}
