package com.v2p.swp391.application.service.impl;

import com.v2p.swp391.application.model.Category;
import com.v2p.swp391.application.repository.CategoryRepository;
import com.v2p.swp391.application.request.CategoryRequest;
import com.v2p.swp391.application.service.CategoryService;
import com.v2p.swp391.exception.ResourceNotFoundException;
import com.v2p.swp391.exception.data.ExistingNameException;
import com.v2p.swp391.utils.LocalizationUtils;
import com.v2p.swp391.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final LocalizationUtils localizationUtils;
    @Override
    public Category createCategory(CategoryRequest categoryRequest) throws Exception {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ExistingNameException(localizationUtils.getLocalizedMessage(MessageKeys.CATEGORY_NAME_EXIST));
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
    public Category updateCategory(long categoryId, CategoryRequest categoryRequest) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryRequest.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}

