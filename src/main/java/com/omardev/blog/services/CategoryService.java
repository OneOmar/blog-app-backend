package com.omardev.blog.services;

import com.omardev.blog.domain.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {

    Category getCategoryById(UUID id);

    List<Category> listCategories();

    Category createCategory(Category category);

    void deleteCategory(UUID id);

}
