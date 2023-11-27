package com.example.auto.services;


import java.security.Principal;
import java.util.Optional;

public interface UserService<UserDTO> extends AbstractService<UserDTO>{
    Optional<UserDTO> get(String username);
    Optional<UserDTO> getByPrincipal(Principal principal);
    void ban(UserDTO dto);
}
