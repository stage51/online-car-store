package com.example.auto.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER(0), ADMIN(1);
    private int role;
    private Role(int role){
        this.role = role;
    }
    @Override
    public String toString() {
        return String.valueOf(role);
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
