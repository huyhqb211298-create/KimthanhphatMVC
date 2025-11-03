package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.ServiceEntity;
import com.example.kimthanhphatmvc.repository.ServiceRepository;
import com.example.kimthanhphatmvc.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<ServiceEntity> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Optional<ServiceEntity> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceRepository.save(serviceEntity);
    }

    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }
}
