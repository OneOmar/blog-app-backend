package com.omardev.blog.services.impl;

import com.omardev.blog.domain.PostStatus;
import com.omardev.blog.domain.entities.Category;
import com.omardev.blog.domain.entities.Post;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.domain.entities.User;
import com.omardev.blog.repositories.PostRepository;
import com.omardev.blog.services.CategoryService;
import com.omardev.blog.services.PostService;
import com.omardev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

}
