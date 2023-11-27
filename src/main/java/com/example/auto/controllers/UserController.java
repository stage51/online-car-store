package com.example.auto.controllers;

import com.example.auto.dtos.UserDTO;
import com.example.auto.utils.ImageUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Controller
public class UserController extends BaseController{
    private ImageUtil imageUtil;
    private PasswordEncoder passwordEncoder;
    public UserController() {

    }
    @Autowired
    public void setImageUtil(ImageUtil imageUtil){this.imageUtil = imageUtil;}
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){this.passwordEncoder = passwordEncoder;}
    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("error", "Username or password are incorrect!");
        return "login";
    }
    @GetMapping("/reg")
    public String registration(Model model) {
        model.addAttribute("user", new UserDTO());
        return "reg";
    }
    @PostMapping("/reg")
    public String createUser(@ModelAttribute @Valid UserDTO user, BindingResult bindingResult,
                             Model model, SessionStatus sessionStatus,
                             @RequestParam("confirmPassword") String confirm) {
        user.setId(new UUID(10, 10).randomUUID());
        user.setBanned(true);
        if (userService.register(user) == null) {
            bindingResult.addError(new ObjectError("isExist", "User is already exist!"));
            model.addAttribute("user", user);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "reg";
        }
        if (isNotValid(user, bindingResult, model, confirm)) return "reg";
        sessionStatus.setComplete();
        return "redirect:/login";
    }
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable UUID id, Model model){
        model.addAttribute("user", (UserDTO) userService.get(id).orElse(null));
        return "users";
    }
    @GetMapping("/user")
    public String getProfile(Model model){
        return "profile";
    }
    @GetMapping("/user/edit")
    public String editProfile(Model model){
        return "edit-user";
    }
    @PostMapping("/user/edit")
    public String saveProfile(@ModelAttribute @Valid UserDTO user, @RequestParam("file") MultipartFile file,
                              BindingResult bindingResult, @RequestParam("confirmPassword") String confirm,
                              Model model, SessionStatus sessionStatus){
        if (isNotValid(user, bindingResult, model, confirm)) return "edit-user";
        user.setImageUrl(imageUtil.saveImage(file));
        userService.update(user);
        sessionStatus.setComplete();
        return "redirect:/user";
    }
    @GetMapping("/user/{id}/edit")
    public String editUser(Model model, @PathVariable UUID id){
        model.addAttribute("user", userService.get(id).orElse(null));
        return "edit-user";
    }
    @PostMapping("/user/{id}/edit")
    public String saveUser(@ModelAttribute UserDTO user, @RequestParam("file") MultipartFile file,
                           Model model, BindingResult bindingResult, @RequestParam("confirmPassword") String confirm,
                           @PathVariable UUID id, SessionStatus sessionStatus){
        if (isNotValid(user, bindingResult, model, confirm)) return "edit-user";
        user.setImageUrl(imageUtil.saveImage(file));
        userService.update(user);
        sessionStatus.setComplete();
        return "redirect:/user/{id}";
    }
    @PostMapping("/user/{id}/ban")
    public String banUser(@PathVariable UUID id, SessionStatus sessionStatus){
        UserDTO user = (UserDTO) userService.get(id).orElse(null);
        userService.ban(user);
        sessionStatus.setComplete();
        return "redirect:/user/{id}";
    }
    @GetMapping("/")
    public String mainPage(){
        return "main";
    }
    private boolean isNotValid(UserDTO user, BindingResult bindingResult, Model model, String confirm) {
        if (!confirm.equals(user.getPassword())) {
            bindingResult.addError(new ObjectError("invalidPassword", "Password is incorrect!"));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return true;
        }
        return false;
    }

}
