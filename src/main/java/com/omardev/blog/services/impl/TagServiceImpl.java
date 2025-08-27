package com.omardev.blog.services.impl;

import com.omardev.blog.domain.dtos.CreateTagsRequest;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.repositories.TagRepository;
import com.omardev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        // Requested names
        Set<String> requestedNames = request.getNames();

        // Fetch existing tags
        Set<Tag> existingTags = tagRepository.findByNameInIgnoreCase(requestedNames);

        // Extract existing names
        Set<String> existingNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        // Determine which names are new
        Set<String> newNames = requestedNames.stream()
                .filter(name -> !existingNames.contains(name))
                .collect(Collectors.toSet());

        // Create Tag objects for new names
        Set<Tag> newTags = newNames.stream()
                .map(name -> Tag.builder().name(name).build())
                .collect(Collectors.toSet());

        // Persist new tags
        Set<Tag> savedTags = new HashSet<>(tagRepository.saveAll(newTags));

        // Return both existing and newly created tags
        savedTags.addAll(existingTags);
        return savedTags;
    }
}
