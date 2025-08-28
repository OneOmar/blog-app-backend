package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.PostDto;
import com.omardev.blog.domain.entities.Post;
import com.omardev.blog.domain.entities.User;
import com.omardev.blog.mappers.PostMapper;
import com.omardev.blog.services.PostService;
import com.omardev.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
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

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        // Fetch the logged-in user
        User loggedInUser = userService.getUserById(userId);

        // Get all draft posts for this user
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);

        // Convert posts to DTOs
        List<PostDto> postDtos = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(postDtos);
    }

}
