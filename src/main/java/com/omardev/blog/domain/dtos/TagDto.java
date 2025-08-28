package com.omardev.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TagDto {

    private final UUID id;
    private final String name;
    private final Integer postCount;

}
