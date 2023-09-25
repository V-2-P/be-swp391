package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.repository.CategoryRepository;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.service.CategoryService;
import com.v2p.swp391.exception.AppException;
import com.v2p.swp391.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(Category categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Category name already exists");
        }
        Category newCategory = Category
                .builder()
                .name(categoryRequest.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, Category category) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}

