package com.omardev.blog.services.impl;

import com.omardev.blog.domain.dtos.CreateTagsRequest;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.repositories.TagRepository;
import com.omardev.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPosts();
    }

    @Override
    @Transactional
    public Set<Tag> createTags(CreateTagsRequest request) {
        Set<String> requestedNames = request.getNames();

        // Fetch existing tags
        Set<Tag> existingTags = tagRepository.findByNameInIgnoreCase(requestedNames);

        // Extract existing names
        Set<String> existingNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        // Determine new names
        Set<String> newNames = requestedNames.stream()
                .filter(name -> !existingNames.contains(name))
                .collect(Collectors.toSet());

        // Create new Tag entities
        Set<Tag> newTags = newNames.stream()
                .map(name -> Tag.builder().name(name).build())
                .collect(Collectors.toSet());

        // Persist new tags
        Set<Tag> savedTags = new HashSet<>(tagRepository.saveAll(newTags));

        // Return both existing and newly created tags
        savedTags.addAll(existingTags);
        return savedTags;
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with associated posts");
            }
            tagRepository.delete(tag);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Tag not found with id: %s", id)
                ));
    }
}
