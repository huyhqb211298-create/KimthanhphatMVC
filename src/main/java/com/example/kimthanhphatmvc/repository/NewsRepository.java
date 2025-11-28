package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findBySlug(String slug);

    List<News> findByActiveTrueOrderByCreatedAtDesc();
    boolean existsBySlug(String slug);
    List<News> findByTitleContainingIgnoreCase(String keyword);
    @Query("SELECT n FROM News n WHERE n.id <> :id ORDER BY n.createdAt DESC")
    List<News> findRelated(@Param("id") Long id, Pageable pageable);

    default List<News> findRelated(Long id, int limit) {
        return findRelated(id, PageRequest.of(0, limit));
    }
    @Query("""
        SELECT n FROM News n
        WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(n.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<News> search(@Param("keyword") String keyword, Pageable pageable);

}
