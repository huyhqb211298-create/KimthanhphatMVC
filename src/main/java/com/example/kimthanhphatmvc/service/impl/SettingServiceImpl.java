package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.Setting;
import com.example.kimthanhphatmvc.repository.SettingRepository;
import com.example.kimthanhphatmvc.service.SettingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    @Override
    public Optional<Setting> findById(Long id) {
        return settingRepository.findById(id);
    }

    @Override
    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public void deleteById(Long id) {
        settingRepository.deleteById(id);
    }
}
