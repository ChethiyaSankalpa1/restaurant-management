package com.restaurant.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModulePermission {
    private String name;
    private String key;
    private String icon;
    private String color;
    private String desc;
    private Boolean managerAccess;
    private Boolean staffAccess;
    
    // Explicit getters for Thymeleaf just in case
    public Boolean getManagerAccess() { return managerAccess; }
    public Boolean getStaffAccess() { return staffAccess; }
}
