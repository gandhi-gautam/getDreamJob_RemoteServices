package in.getdreamjob.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Qualifications {
    B_TECH_AND_M_TECH("B.Tech/M.tech"),
    BCA_AND_MCA("Bca / Mca"),
    BBA_AND_MBA("Bba / Mba"),
    BE_AND_ME("BE / ME"),
    Any_Degree("Any degree");

    private final String displayName;

    private Qualifications(String displayName) {
        this.displayName = displayName;
    }

    List<String> allQualificationsByDisplayName() {
        List<String> result = new ArrayList<>();
        Qualifications[] qualifications = Qualifications.values();
        for (Qualifications qualification : qualifications) {
            result.add(qualification.displayName);
        }
        return result;
    }
}
