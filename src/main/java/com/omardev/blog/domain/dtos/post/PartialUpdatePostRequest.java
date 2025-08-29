package com.omardev.blog.domain.dtos.post;

import com.omardev.blog.domain.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartialUpdatePostRequest {

    private String title;
    private String content;
    private UUID categoryId;
    private Set<UUID> tagIds;
    private PostStatus status;
}
