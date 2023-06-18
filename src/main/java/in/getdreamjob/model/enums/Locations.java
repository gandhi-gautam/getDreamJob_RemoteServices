package in.getdreamjob.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Locations {
    PUNE("PUN"),
    BANGALORE("BLR"),
    DELHI("NCR"),
    HYDERABAD("HYD"),
    MUMBAI("MUM"),
    CHENNAI("CHN"),
    KOLKATA("KL"),
    INDIA("IND");

    private String displayName;


    Locations(String displayName) {
        this.displayName = displayName;
    }

    List<String> allLocationsByDisplayName() {
        List<String> result = new ArrayList<>();
        Locations[] locations = Locations.values();
        for (Locations location : locations) {
            result.add(location.displayName);
        }
        return result;
    }
}
