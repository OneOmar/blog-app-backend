package com.omardev.blog.services.impl;

import com.omardev.blog.domain.entities.Category;
import com.omardev.blog.repositories.CategoryRepository;
import com.omardev.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPosts();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        // Check if category with same name already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }
}
