package com.omardev.blog.controllers;

import com.omardev.blog.domain.dtos.TagResponseDto;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.mappers.TagMapper;
import com.omardev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
