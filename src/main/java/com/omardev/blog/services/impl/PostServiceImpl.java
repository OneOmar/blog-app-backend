package com.omardev.blog.services.impl;

import com.omardev.blog.domain.PostStatus;
import com.omardev.blog.domain.dtos.CreatePostRequest;
import com.omardev.blog.domain.dtos.UpdatePostRequest;
import com.omardev.blog.domain.entities.Category;
import com.omardev.blog.domain.entities.Post;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.domain.entities.User;
import com.omardev.blog.repositories.PostRepository;
import com.omardev.blog.services.CategoryService;
import com.omardev.blog.services.PostService;
import com.omardev.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        Category category = null;
        Tag tag = null;

        if (categoryId != null) {
            category = categoryService.getCategoryById(categoryId);
        }

        if (tagId != null) {
            tag = tagService.getTagById(tagId);
        }

        if (category != null && tag != null) {
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED, category, tag
            );
        }

        if (category != null) {
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tag != null) {
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User author, CreatePostRequest request) {

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);
        post.setStatus(request.getStatus());
        post.setReadingTime(calculateReadingTime(request.getContent()));

        // Set category
        Category category = categoryService.getCategoryById(request.getCategoryId());
        post.setCategory(category);

        // Set tags
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        post.setTags(tags);

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(User author, UpdatePostRequest request) {
        // Load the existing post (404 if not found)
        Post post = postRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Post not found with id: " + request.getId()
                ));

        // Authorization: only the owner can update their post
        if (!post.getAuthor().getId().equals(author.getId())) {
            throw new AccessDeniedException("You are not allowed to update this post");
        }

        // Update scalar fields
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setStatus(request.getStatus());

        // Recalculate reading time
        post.setReadingTime(calculateReadingTime(request.getContent()));

        // Update category
        Category category = categoryService.getCategoryById(request.getCategoryId());
        post.setCategory(category);

        // Update tags
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        post.setTags(tags);

        // JPA @PreUpdate will handle updatedAt
        return postRepository.save(post);
    }

    // Helper method to estimate reading time
    private int calculateReadingTime(String content) {
        int words = content.split("\\s+").length;
        int wordsPerMinute = 200; // average reading speed
        return Math.max(1, words / wordsPerMinute);
    }

}
