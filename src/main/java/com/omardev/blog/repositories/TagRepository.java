package com.omardev.blog.repositories;

import com.omardev.blog.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Query("SELECT DISTINCT t FROM Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithPosts();

    Set<Tag> findByNameInIgnoreCase(Set<String> names);

    boolean existsByName(String name);
}
