package com.omardev.blog.services;

import com.omardev.blog.domain.dtos.CreateTagsRequest;
import com.omardev.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    Tag getTagById(UUID id);

    List<Tag> getAllTags();

    Set<Tag> createTags(CreateTagsRequest request);

    void deleteTag(UUID id);

    Set<Tag> getTagsByIds(Set<UUID> ids);

}
