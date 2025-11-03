package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Contact;
import com.example.kimthanhphatmvc.repository.ContactRepository;
import com.example.kimthanhphatmvc.service.ContactService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }
}
