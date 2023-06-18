package in.getdreamjob.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    FRESHER("Fresher"),
    INTERNSHIP("Internship"),
    EXPERIENCE("Experience");

    private String displayName;


    Category(String displayName) {
        this.displayName = displayName;
    }

    List<String> allCategoriesByDisplayName() {
        List<String> result = new ArrayList<>();
        Category[] categories = Category.values();
        for (Category category : categories) {
            result.add(category.displayName);
        }
        return result;
    }
}
