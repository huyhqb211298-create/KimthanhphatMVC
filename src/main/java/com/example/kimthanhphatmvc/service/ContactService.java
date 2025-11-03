package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.Contact;
import java.util.List;
import java.util.Optional;

public interface ContactService {
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    Contact save(Contact contact);
    void deleteById(Long id);
}
