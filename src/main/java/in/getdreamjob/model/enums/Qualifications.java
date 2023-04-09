package in.getdreamjob.model.enums;

public enum Qualifications {
    B_TECH("b.tech"),
    MCA("mca"),
    MBA("mba"),
    M_TECH("m.tech"),
    Any_Degree("any degree");

    private final String displayName;

    private Qualifications(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
