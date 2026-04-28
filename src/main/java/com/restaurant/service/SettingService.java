package com.restaurant.service;

import com.restaurant.model.SystemSetting;
import com.restaurant.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SystemSettingRepository systemSettingRepository;

    public String getSetting(String key, String defaultValue) {
        return systemSettingRepository.findByKey(key)
                .map(SystemSetting::getValue)
                .orElse(defaultValue);
    }

    public void saveSetting(String key, String value) {
        SystemSetting setting = systemSettingRepository.findByKey(key)
                .orElse(SystemSetting.builder().key(key).build());
        setting.setValue(value);
        systemSettingRepository.save(setting);
    }

    public Map<String, String> getAllSettings() {
        return systemSettingRepository.findAll().stream()
                .collect(Collectors.toMap(SystemSetting::getKey, SystemSetting::getValue));
    }
}
