package com.example.auto.services;

import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        com.example.auto.models.User user = userRepository.findByUsername(username).get();
        if (user != null) {
            if (user.getBanned()){
                return User.withUsername(user.getUsername()).accountLocked(true).build();
            }
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getUserRole().getRole().name());
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        } else {
            throw new EntityNotFoundException("User", username);
        }
    }
}

