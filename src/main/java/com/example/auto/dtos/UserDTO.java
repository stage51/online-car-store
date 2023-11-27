package com.example.auto.dtos;

import com.example.auto.models.UserRole;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
public class UserDTO extends BaseDTO{
    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean banned;
    private UserRole userRole;
    private String imageUrl;
    private Set<OfferDTO> offers;
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Username length must be more than two characters!")
    public String getUsername() {
        return username;
    }
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Password length must be more than two characters!")
    public String getPassword() {
        return password;
    }
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "First name length must be more than two characters!")
    public String getFirstName() {
        return firstName;
    }
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Last name length must be more than two characters!")
    public String getLastName() {
        return lastName;
    }

    public boolean getBanned() {
        return banned;
    }
}
