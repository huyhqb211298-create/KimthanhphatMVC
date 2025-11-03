package com.example.kimthanhphatmvc.repository;

import com.example.kimthanhphatmvc.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
}
