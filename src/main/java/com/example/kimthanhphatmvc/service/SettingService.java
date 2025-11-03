package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.Setting;
import java.util.List;
import java.util.Optional;

public interface SettingService {
    List<Setting> findAll();
    Optional<Setting> findById(Long id);
    Setting save(Setting setting);
    void deleteById(Long id);
}
