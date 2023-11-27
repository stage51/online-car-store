package com.example.auto.models;

import com.example.auto.models.base.BaseEntity;
import com.example.auto.models.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity {
    public UserRole() {
    }
    private Role role;
    @Enumerated(EnumType.ORDINAL)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
