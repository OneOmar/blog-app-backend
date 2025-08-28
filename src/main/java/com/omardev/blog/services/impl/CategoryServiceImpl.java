package com.omardev.blog.services.impl;

import com.omardev.blog.domain.entities.Category;
import com.omardev.blog.repositories.CategoryRepository;
import com.omardev.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        // Check if category with the same name already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);

        if (!category.getPosts().isEmpty()) {
            throw new IllegalStateException(
                    String.format("Cannot delete category '%s': it has associated posts.", category.getName())
            );
        }

        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Category not found with id: %s", id)
                ));
    }
}
