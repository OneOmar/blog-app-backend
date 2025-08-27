package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.CreateTagsRequest;
import com.omardev.blog.domain.dtos.TagResponseDto;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.mappers.TagMapper;
import com.omardev.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/va/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        List<TagResponseDto> tagDtos = tagMapper.toDtoList(tags);
        return ResponseEntity.ok(tagDtos);
    }


    @PostMapping
    public ResponseEntity<Set<TagResponseDto>> createTags(
            @Valid @RequestBody CreateTagsRequest request
    ) {
        Set<Tag> tags = tagService.createTags(request);
        return ResponseEntity.ok(
                tags.stream()
                        .map(tagMapper::toDto)
                        .collect(Collectors.toSet())
        );
    }
}
