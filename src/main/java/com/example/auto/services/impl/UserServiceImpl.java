package com.example.auto.services.impl;

import com.example.auto.dtos.UserDTO;
import com.example.auto.exceptions.EntityIsExistException;
import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.models.User;
import com.example.auto.models.UserRole;
import com.example.auto.models.enums.Role;
import com.example.auto.repos.UserRepository;
import com.example.auto.services.UserService;
import com.example.auto.utils.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class UserServiceImpl implements UserService<UserDTO> {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserDTO register(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        if (!this.validationUtil.isValid(dto)) {
            return null;
        } else if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
            UserRole userRole = new UserRole();
            userRole.setRole(Role.USER);
            user.setUserRole(userRole);
            user.setCreated(new Date());
            user.setModified(new Date());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.saveAndFlush(user);
            return modelMapper.map(user, UserDTO.class);
        } else {
            throw new EntityIsExistException("User is already exists.");
        }
    }


    @Override
    @Cacheable("users")
    public Optional<UserDTO> get(UUID id) {
        return Optional.ofNullable(modelMapper.map(userRepository.findById(id), UserDTO.class));
    }
    @Override
    @Cacheable("users")
    public Optional<UserDTO> get(String username) {
        return Optional.ofNullable(modelMapper.map(userRepository.findByUsername(username), UserDTO.class));
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public UserDTO update(UserDTO dto) {
        Optional<User> userFromRepository = userRepository.findById(dto.getId());
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (userFromRepository.isPresent()){
            User user = modelMapper.map(dto, User.class);
            user.setUserRole(userFromRepository.get().getUserRole());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreated(userFromRepository.get().getCreated());
            user.setModified(new Date());
            return modelMapper.map(userRepository.saveAndFlush(user), UserDTO.class);
        }
        else{
            throw new EntityNotFoundException("User", dto.getId(), "update");
        }
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)

    public void delete(UUID id) {
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("User", id, "delete");
        }
    }

    @Override
    @Cacheable("users")
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map((s) -> modelMapper.map(s, UserDTO.class)).collect(Collectors.toList());
    }
    @Override
    @Cacheable("users")
    public Optional<UserDTO> getByPrincipal(Principal principal) {
        return Optional.ofNullable(modelMapper.map(userRepository.findByUsername(principal.getName()), UserDTO.class));
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void ban(UserDTO dto) {
        Optional<User> userFromRepository = userRepository.findById(dto.getId());
        if (userFromRepository.isPresent()){
            User user = modelMapper.map(dto, User.class);
            user.setUserRole(userFromRepository.get().getUserRole());
            user.setCreated(userFromRepository.get().getCreated());
            user.setModified(new Date());
            if (userFromRepository.get().getBanned()){
                user.setBanned(false);
            }
            else {
                user.setBanned(true);
            }
            modelMapper.map(userRepository.saveAndFlush(user), UserDTO.class);
        }
        else {
            throw new EntityNotFoundException("User", dto.getId(), "ban");
        }
    }

}
