package com.restaurant.controller;

import com.restaurant.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final SettingService settingService;

    @ModelAttribute("currencySymbol")
    public String getCurrencySymbol() {
        return settingService.getSetting("currency_symbol", "$");
    }

    @ModelAttribute("currencyName")
    public String getCurrencyName() {
        return settingService.getSetting("currency_name", "USD");
    }
}
