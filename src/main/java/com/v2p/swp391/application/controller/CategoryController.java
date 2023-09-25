package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.response.CategoryResponse;
import com.v2p.swp391.application.service.CategoryService;
import com.v2p.swp391.exception.data.ExistingNameException;
import com.v2p.swp391.utils.LocalizationUtils;
import com.v2p.swp391.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {

    private final LocalizationUtils localizationUtils;
    private final CategoryService categoryService;
    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
            ) throws Exception {

        CategoryResponse categoryResponse = new CategoryResponse();

        try {
            Category category = categoryService.createCategory(categoryRequest);
            categoryResponse.setCategory(category);
            categoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY));
            return ResponseEntity.ok(categoryResponse);
        } catch (ExistingNameException e) {
            categoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED));
            categoryResponse.setErrors(e.getMessage());
            return ResponseEntity.badRequest().body(categoryResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page")     int page,
            @RequestParam("limit")    int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryDTO
    ) {
        CategoryResponse updateCategoryResponse = new CategoryResponse();

        Category category = categoryService.updateCategory(id, categoryDTO);
        updateCategoryResponse.setCategory(category);
        updateCategoryResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
        return ResponseEntity.ok(updateCategoryResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }

}

