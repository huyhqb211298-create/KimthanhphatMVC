package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    // Bạn có thể thêm các query tùy chỉnh nếu cần, ví dụ:
    // List<ServiceEntity> findByNameContainingIgnoreCase(String name);
}
