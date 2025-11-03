package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
