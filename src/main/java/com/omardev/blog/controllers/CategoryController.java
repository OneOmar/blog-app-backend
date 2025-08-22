package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.CategoryDto;
import com.omardev.blog.mappers.CategoryMapper;
import com.omardev.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(
                categoryMapper.toDto(categoryService.listCategories())
        );
    }
}
