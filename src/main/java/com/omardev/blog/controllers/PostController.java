package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.PostDto;
import com.omardev.blog.mappers.PostMapper;
import com.omardev.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {

        return ResponseEntity.ok(
                postService.getAllPosts(categoryId, tagId)
                        .stream()
                        .map(postMapper::toDto)
                        .toList()
        );
    }
}
