package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.ServiceEntity;
import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<ServiceEntity> findAll();
    Optional<ServiceEntity> findById(Long id);
    ServiceEntity save(ServiceEntity serviceEntity);
    void deleteById(Long id);
}
