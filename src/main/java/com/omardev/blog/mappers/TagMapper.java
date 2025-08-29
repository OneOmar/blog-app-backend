package com.omardev.blog.mappers;

import com.omardev.blog.domain.enums.PostStatus;
import com.omardev.blog.domain.dtos.tag.TagDto;
import com.omardev.blog.domain.entities.Tag;
import com.omardev.blog.domain.entities.Post;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toDto(Tag tag);

    List<TagDto> toDtoList(List<Tag> tags);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (posts == null) return 0;
        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
