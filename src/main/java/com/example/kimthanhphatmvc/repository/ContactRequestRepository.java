package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.ContactRequest;
import com.example.kimthanhphatmvc.model.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    Page<ContactRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<ContactRequest> findByStatusOrderByCreatedAtDesc(ContactStatus status, Pageable pageable);
}
