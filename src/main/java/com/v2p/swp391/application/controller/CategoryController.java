package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.response.CategoryResponse;
import com.v2p.swp391.application.service.CategoryService;
import com.v2p.swp391.common.api.CoreApiResponse;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.data.ExistingNameException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("")
    public CoreApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
            ) throws Exception {
        CategoryResponse categoryResponse = new CategoryResponse();
        try {
            Category category = categoryService.createCategory(categoryRequest);
            categoryResponse.setMessage("Insert category successfully");
            categoryResponse.setCategory(category);
            return CoreApiResponse.success(categoryResponse);
        } catch (AppException e) {
            categoryResponse.setMessage("Failed to create new category");
            categoryResponse.setErrors(e.getMessage());
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST,categoryResponse);
        }
    }

    @GetMapping("")
    public CoreApiResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return CoreApiResponse.success(categories);
    }
    @PutMapping("/{id}")
    public CoreApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryDTO
    ) {
        CategoryResponse updateCategoryResponse = new CategoryResponse();
        Category category = categoryService.updateCategory(id, categoryDTO);
        updateCategoryResponse.setMessage("Update category successfully");
        updateCategoryResponse.setCategory(category);
        return CoreApiResponse.success(updateCategoryResponse);
    }
    @DeleteMapping("/{id}")
    public CoreApiResponse<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return CoreApiResponse.success("Delete category with id: " + id + " successfully");
    }

}

