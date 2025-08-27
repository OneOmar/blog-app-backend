package com.omardev.blog.services;

import com.omardev.blog.domain.dtos.CreateTagsRequest;
import com.omardev.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<Tag> getAllTags();
    Set<Tag> createTags(CreateTagsRequest request);
}
