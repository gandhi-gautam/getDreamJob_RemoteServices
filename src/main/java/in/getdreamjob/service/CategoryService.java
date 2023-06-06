package in.getdreamjob.service;

import in.getdreamjob.model.Category;
import in.getdreamjob.model.GeneralResponse;

public interface CategoryService {
    public GeneralResponse createNewCategory(Category category);

    public GeneralResponse updateCategory(Category category);

    public GeneralResponse getAllCategory();

    public GeneralResponse getCategory(long categoryId);

    public GeneralResponse deleteCategoryFromAJob(long jobId, long categoryId);

    public GeneralResponse deleteCategory(long categoryId);

    public GeneralResponse connectCategoryFromJob(long jobId, long categoryId);
}
