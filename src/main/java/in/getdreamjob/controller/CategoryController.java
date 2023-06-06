package in.getdreamjob.controller;

import in.getdreamjob.model.Category;
import in.getdreamjob.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createNewCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createNewCategory(category), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.updateCategory(category), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
    }

    @GetMapping("/{categoryName}/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryName, @PathVariable long categoryId) {
        return new ResponseEntity<>(categoryService.getCategory(categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/{jobId}/{categoryId}")
    public ResponseEntity<?> deleteCategoryFromJob(@PathVariable long jobId, @PathVariable long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategoryFromAJob(jobId, categoryId), HttpStatus.OK);
    }

    @PostMapping("/{jobId}/{categoryId}")
    public ResponseEntity<?> connectCategoryFromJob(@PathVariable long jobId, @PathVariable long categoryId) {
        return new ResponseEntity<>(categoryService.connectCategoryFromJob(jobId, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
