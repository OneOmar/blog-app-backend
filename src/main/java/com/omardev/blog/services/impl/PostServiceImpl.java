package com.omardev.blog.services.impl;

import com.omardev.blog.domain.enums.PostStatus;
import com.omardev.blog.domain.dtos.post.CreatePostRequest;
import com.omardev.blog.domain.dtos.post.PartialUpdatePostRequest;
import com.omardev.blog.domain.dtos.post.UpdatePostRequest;
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

        // Fetch the existing post by ID
        Post post = postRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Post not found with id: " + request.getId()
                ));

        // Authorization: only the owner can update their post
        if (!post.getAuthor().getId().equals(author.getId())) {
            throw new AccessDeniedException("You are not allowed to update this post");
        }

        // Update post fields
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setStatus(request.getStatus());

        // Update category
        Category category = categoryService.getCategoryById(request.getCategoryId());
        post.setCategory(category);

        // Update tags
        Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
        post.setTags(tags);

        // Recalculate reading time if needed
        post.setReadingTime(calculateReadingTime(post.getContent()));

        // updatedAt will be updated automatically by @PreUpdate
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post partialUpdatePost(User author, UUID postId, PartialUpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Authorization check
        if (!post.getAuthor().getId().equals(author.getId())) {
            throw new AccessDeniedException("You are not allowed to update this post");
        }

        // Update fields only if present
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getStatus() != null) post.setStatus(request.getStatus());
        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(request.getCategoryId());
            post.setCategory(category);
        }
        if (request.getTagIds() != null) {
            Set<Tag> tags = tagService.getTagsByIds(request.getTagIds());
            post.setTags(tags);
        }

        // Recalculate reading time if content updated
        if (request.getContent() != null) {
            post.setReadingTime(calculateReadingTime(post.getContent()));
        }

        return postRepository.save(post);
    }


    // Helper method to estimate reading time
    private int calculateReadingTime(String content) {
        int words = content.split("\\s+").length;
        int wordsPerMinute = 200; // average reading speed
        return Math.max(1, words / wordsPerMinute);
    }

    @Override
    @Transactional
    public void deletePost(User author, UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Authorization: only the author can delete their post
        if (!post.getAuthor().getId().equals(author.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

}
