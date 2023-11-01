package com.v2p.swp391.application.controller;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.service.CategoryService;
import com.v2p.swp391.common.api.CoreApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.v2p.swp391.application.mapper.CategoryHttpMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("")
    public CoreApiResponse<Category> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
    ){
            Category categoryResponse = categoryService.createCategory(INSTANCE.toModel(categoryRequest));
            return CoreApiResponse.success(categoryResponse,"Insert category successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return CoreApiResponse.success(categories);
    }
    @PutMapping("/{id}")
    public CoreApiResponse<Category> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        Category category = categoryService.updateCategory(id, (INSTANCE.toModel(categoryRequest)));
        return CoreApiResponse.success(category,"Update category successfully");
    }
    @DeleteMapping("/{id}")
    public CoreApiResponse<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return CoreApiResponse.success("Delete category with id: " + id + " successfully");
    }

}

