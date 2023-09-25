package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest category) throws Exception;
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryRequest category);
    void deleteCategory(long id);
}


