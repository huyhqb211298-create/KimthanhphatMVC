package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
