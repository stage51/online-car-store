package com.example.auto.services.impl;

import com.example.auto.exceptions.EntityIsExistException;
import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.models.UserRole;
import com.example.auto.repos.UserRoleRepository;
import com.example.auto.services.UserRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class UserRoleServiceImpl implements UserRoleService<UserRole> {

    private UserRoleRepository userRoleRepository;

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole register(UserRole userRole) {
        if (userRoleRepository.findById(userRole.getId()) == null){
            return userRoleRepository.saveAndFlush(userRole);
        }
        else{
            throw new EntityIsExistException("UserRole is already exists.");
        }
    }
    @Override
    public Optional<UserRole> get(UUID id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public UserRole update(UserRole userRole) {
        if (userRoleRepository.findById(userRole.getId()).isPresent()){
            return userRoleRepository.saveAndFlush(userRole);
        }
        else{
            throw new EntityNotFoundException("UserRole", userRole.getId(), "update");
        }
    }

    @Override
    public void delete(UUID id) {
        if (userRoleRepository.findById(id).isPresent()){
            userRoleRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("UserRole", id, "delete");
        }
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }
}
