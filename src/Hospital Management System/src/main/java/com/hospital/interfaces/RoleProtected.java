package com.hospital.interfaces;

import com.hospital.enums.UserRole;

public interface RoleProtected {

    UserRole[] getAllowedRoles();

    default boolean isAllowed(UserRole role) {
        for (UserRole allowed : getAllowedRoles()) {
            if (allowed == role) return true;
        }
        return false;
    }
}