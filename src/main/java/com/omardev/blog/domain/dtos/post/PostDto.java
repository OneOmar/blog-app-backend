package com.omardev.blog.domain.dtos.post;

import com.omardev.blog.domain.enums.PostStatus;
import com.omardev.blog.domain.dtos.tag.TagDto;
import com.omardev.blog.domain.dtos.author.AuthorDto;
import com.omardev.blog.domain.dtos.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PostDto {

    private UUID id;
    private String title;
    private String content;
    private AuthorDto author;
    private CategoryDto category;
    private Set<TagDto> tags;
    private Integer readingTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus status;

}
