package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.ContactRequest;
import com.example.kimthanhphatmvc.model.enums.ContactStatus;
import com.example.kimthanhphatmvc.repository.ContactRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactRequestService {

    private static final int PAGE_SIZE = 20;

    private final ContactRequestRepository repository;
    private final ContactNotificationService notificationService;

    public ContactRequestService(ContactRequestRepository repository,
                                 ContactNotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public ContactRequest create(String name, String email, String phone,
                                 String subject, String message) {
        ContactRequest request = new ContactRequest();
        request.setName(name.strip());
        request.setEmail(email.strip().toLowerCase());
        request.setPhone(phone.strip());
        request.setSubject(subject == null ? null : subject.strip());
        request.setMessage(message.strip());

        request = repository.saveAndFlush(request);

        ContactNotificationService.NotificationResult result = notificationService.send(request);
        request.setEmailNotificationSent(result.sent());
        request.setNotificationError(limit(result.error(), 500));
        return repository.save(request);
    }

    @Transactional(readOnly = true)
    public Page<ContactRequest> findPage(int page, ContactStatus status) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE);
        return status == null
                ? repository.findAllByOrderByCreatedAtDesc(pageable)
                : repository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Transactional
    public boolean updateStatus(Long id, ContactStatus status) {
        return repository.findById(id).map(request -> {
            request.setStatus(status);
            repository.save(request);
            return true;
        }).orElse(false);
    }

    private String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
