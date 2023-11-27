package com.example.auto.controllers;

import com.example.auto.dtos.UserDTO;
import com.example.auto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public class BaseController {
    protected UserService userService;
    @Autowired
    public void setUserService(UserService userService){this.userService = userService;}
    @ModelAttribute("currentUser")
    public UserDTO currentUser(Principal principal){
        if (principal == null){
            return null;
        }
        return (UserDTO) userService.getByPrincipal(principal).orElse(null);
    }
}
