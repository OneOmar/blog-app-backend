package com.omardev.blog.repositories;

import com.omardev.blog.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPosts();
}


//@Repository
//public interface CategoryRepository extends JpaRepository<Category, UUID> {
//    @Query("""
//    SELECT c, COUNT(p)\s
//    FROM Category c\s
//    LEFT JOIN c.posts p ON p.status = PostStatus.PUBLISHED
//    GROUP BY c""")
//
//    List<Category> findAllWithPostCount();
//}
