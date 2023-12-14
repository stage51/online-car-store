package com.example.auto.controllers;

import com.example.auto.dtos.BaseDTO;
import com.example.auto.dtos.BrandDTO;
import com.example.auto.dtos.UserDTO;
import com.example.auto.services.BrandService;
import com.example.auto.services.UserService;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/brands")
public class BrandController extends BaseController {
    protected static final Logger log = LoggerFactory.getLogger(BrandController.class);

    private BrandService brandService;
    public BrandController() {
    }
    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
    @GetMapping
    public String viewAllBrands(Model model) {
        log.info("Getting all brands", Level.INFO);
        List<BrandDTO> brands = brandService.getAll();
        model.addAttribute("brands", brands);
        return "brands";
    }
    @GetMapping("/create")
    public String showCreateBrandForm(Model model, @ModelAttribute BrandDTO brand) {
        log.info("Open form for new brand", Level.INFO);
        model.addAttribute("brand", brand);
        return "add-brand";
    }
    @PostMapping("/create")
    public String createBrand(@ModelAttribute @Valid BrandDTO brand,
                              BindingResult bindingResult,
                              RedirectAttributes re,
                              SessionStatus sessionStatus,
                              Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Brand form has errors", Level.ERROR);
            re.addFlashAttribute("brand", brand);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/brands/create";
        }
        brand.setId(new UUID(10, 10).randomUUID());
        brandService.register(brand);
        log.info("Created new brand", Level.INFO);
        sessionStatus.setComplete();
        return "redirect:/brands";
    }

    @GetMapping("/edit/{id}")
    public String showEditBrandForm(@PathVariable UUID id, Model model) {
        Optional<BrandDTO> brand = brandService.get(id);
        model.addAttribute("brand", brand.orElse(null));
        log.info("Open form for update brand " + id, Level.INFO);
        return "edit-brand";
    }
    @PostMapping("/edit")
    public String editBrand(@ModelAttribute @Valid BrandDTO brand,
                            BindingResult bindingResult,
                            SessionStatus sessionStatus,
                            RedirectAttributes re,
                            Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Brand form has errors", Level.ERROR);
            re.addFlashAttribute("brand", brand);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/brands/edit/" + brand.getId();
        }
        brandService.update(brand);
        log.info("Updating brand", Level.INFO);
        sessionStatus.setComplete();
        return "redirect:/brands";
    }
    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable UUID id) {
        brandService.delete(id);
        log.info("Deleting brand " + id, Level.INFO);
        return "redirect:/brands";
    }
}
