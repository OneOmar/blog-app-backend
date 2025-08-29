package com.omardev.blog.services;


import com.omardev.blog.domain.dtos.CreatePostRequest;
import com.omardev.blog.domain.dtos.UpdatePostRequest;
import com.omardev.blog.domain.entities.Post;
import com.omardev.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId, UUID tagId);

    List<Post> getDraftPosts(User user);

    Post createPost(User author, CreatePostRequest request);

    Post updatePost(User author, UpdatePostRequest request);

}
