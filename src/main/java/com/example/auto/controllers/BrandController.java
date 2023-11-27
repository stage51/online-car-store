package com.example.auto.controllers;

import com.example.auto.dtos.BaseDTO;
import com.example.auto.dtos.BrandDTO;
import com.example.auto.dtos.UserDTO;
import com.example.auto.services.BrandService;
import com.example.auto.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/brands")
public class BrandController extends BaseController {

    private BrandService brandService;
    public BrandController() {
    }
    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
    @GetMapping
    public String viewAllBrands(Model model) {
        List<BrandDTO> brands = brandService.getAll();
        model.addAttribute("brands", brands);
        return "brands";
    }
    @GetMapping("/create")
    public String showCreateBrandForm(Model model) {
        model.addAttribute("brand", new BrandDTO());
        return "add-brand";
    }
    @PostMapping("/create")
    public String createBrand(@ModelAttribute @Valid BrandDTO brand,
                              BindingResult bindingResult,
                              SessionStatus sessionStatus,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brand", brand);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "add-brand";
        }
        brand.setId(new UUID(10, 10).randomUUID());
        brandService.register(brand);
        sessionStatus.setComplete();
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String showEditBrandForm(@PathVariable UUID id, Model model) {
        Optional<BrandDTO> brand = brandService.get(id);
        model.addAttribute("brand", brand.orElse(null));
        return "edit-brand";
    }
    @PostMapping("/edit")
    public String editBrand(@ModelAttribute @Valid BrandDTO brand,
                            BindingResult bindingResult,
                            SessionStatus sessionStatus,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brand", brand);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "edit-brand";
        }
        brandService.update(brand);
        sessionStatus.setComplete();
        return "redirect:/brands";
    }
    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable UUID id) {
        brandService.delete(id);
        return "redirect:/brands";
    }
}
