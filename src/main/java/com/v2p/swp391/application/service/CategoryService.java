package com.v2p.swp391.application.service;

import com.v2p.swp391.application.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category) ;
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, Category category);
    void deleteCategory(long id);
}


