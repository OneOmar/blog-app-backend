package com.omardev.blog.mappers;

import com.omardev.blog.domain.PostStatus;
import com.omardev.blog.domain.dtos.CategoryDto;
import com.omardev.blog.domain.dtos.CreateCategoryRequest;
import com.omardev.blog.domain.entities.Category;
import com.omardev.blog.domain.entities.Post;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "countPublishedPosts")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("countPublishedPosts")
    default long countPublishedPosts(List<Post> posts) {
        if (posts == null) return 0;
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
