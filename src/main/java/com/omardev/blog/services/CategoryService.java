package com.omardev.blog.services;

import com.omardev.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listCategories();
    Category createCategory(Category category);

}
