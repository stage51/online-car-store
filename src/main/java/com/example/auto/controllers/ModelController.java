package com.example.auto.controllers;

import com.example.auto.dtos.ModelDTO;
import com.example.auto.dtos.UserDTO;
import com.example.auto.models.enums.Category;
import com.example.auto.services.BrandService;
import com.example.auto.services.ModelService;
import com.example.auto.services.UserService;
import com.example.auto.utils.ImageUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/models")
public class ModelController extends BaseController{
    private ModelService modelService;
    private BrandService brandService;
    private ImageUtil imageUtil;

    public ModelController() {
    }
    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
    @Autowired
    public void setImageUtil(ImageUtil imageUtil) {this.imageUtil = imageUtil;}

    @GetMapping
    public String viewAllModels(Model model) {
        List<ModelDTO> models = modelService.getAll();
        model.addAttribute("models", models);
        return "models";
    }
    @GetMapping("/create")
    public String showCreateModelForm(Model model, @ModelAttribute ModelDTO modelDTO) {
        model.addAttribute("model", modelDTO);
        model.addAttribute("categories", Category.values());
        model.addAttribute("brands", brandService.getAll());
        return "add-model";
    }
    @PostMapping("/create")
    public String createModel(@ModelAttribute @Valid ModelDTO modelDTO,
                              BindingResult bindingResult, RedirectAttributes re,
                              @RequestParam("file") MultipartFile file, SessionStatus sessionStatus, Model model) {
        modelDTO.setId(new UUID(10, 10).randomUUID());
        if (bindingResult.hasErrors()) {
            re.addFlashAttribute("model", modelDTO);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/models/create";
        }
        modelDTO.setImageUrl(imageUtil.saveImage(file));
        modelService.register(modelDTO);
        sessionStatus.setComplete();
        return "redirect:/models/page/1";
    }
    @GetMapping("/edit/{id}")
    public String showEditModelForm(@PathVariable UUID id, Model model, @ModelAttribute ModelDTO modelDTO) {
        Optional<ModelDTO> modelToEdit = modelService.get(id);
        model.addAttribute("model", modelToEdit.orElse(null));
        model.addAttribute("categories", Category.values());
        model.addAttribute("brands", brandService.getAll());
        return "edit-model";
    }
    @PostMapping("/edit")
    public String editModel(@ModelAttribute @Valid ModelDTO modelDTO,
                            BindingResult bindingResult, RedirectAttributes re,
                            @RequestParam("file") MultipartFile file, SessionStatus sessionStatus, Model model) {
        if (bindingResult.hasErrors()) {
            re.addFlashAttribute("model", modelDTO);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/models/edit/" + modelDTO.getId();
        }
        modelDTO.setImageUrl(imageUtil.saveImage(file));
        modelService.update(modelDTO);
        sessionStatus.setComplete();
        return "redirect:/models/page/1";
    }
    @GetMapping("/delete/{id}")
    public String deleteModel(@PathVariable UUID id) {
        modelService.delete(id);
        return "redirect:/models";
    }
    @GetMapping("/page/{page}")
    public String pageModels(@PathVariable Integer page, Model model,
                             @RequestParam(value = "name", required = false) String name){
        Page<ModelDTO> pageList = modelService.getPageByBrandName(page - 1, name);
        model.addAttribute("models", pageList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        return "page-models";
    }
}
