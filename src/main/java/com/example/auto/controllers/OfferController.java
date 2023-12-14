package com.example.auto.controllers;


import com.example.auto.dtos.OfferDTO;
import com.example.auto.dtos.UserDTO;
import com.example.auto.models.enums.Engine;
import com.example.auto.models.enums.Transmission;
import com.example.auto.services.ModelService;
import com.example.auto.services.OfferService;
import com.example.auto.services.UserService;
import com.example.auto.utils.ImageUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
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
@RequestMapping("/offers")
public class OfferController extends BaseController{
    protected static final Logger log = LoggerFactory.getLogger(OfferController.class);

    private OfferService offerService;

    private ModelService modelService;

    private ImageUtil imageUtil;
    public OfferController() {
    }
    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }
    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
    @Autowired
    public void setImageUtil(ImageUtil imageUtil){this.imageUtil = imageUtil;}
    @GetMapping
    public String viewAllOffers(Model model) {
        log.info("Getting all offers", Level.INFO);
        List<OfferDTO> offers = offerService.getAll();
        model.addAttribute("offers", offers);
        return "offers";
    }
    @GetMapping("/create")
    public String showCreateOfferForm(Model model, @ModelAttribute OfferDTO offer) {
        log.info("Open form for new offer", Level.INFO);
        model.addAttribute("offer", offer);
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("engines", Engine.values());
        model.addAttribute("transmissions", Transmission.values());

        return "add-offer";
    }
    @PostMapping("/create")
    public String createOffer(@ModelAttribute @Valid OfferDTO offer, BindingResult bindingResult,
                              Model model, RedirectAttributes re,
                              @RequestParam("file") MultipartFile file, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            log.info("Offer form has errors", Level.ERROR);
            re.addFlashAttribute("offer", offer);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/offers/create";
        }
        offer.setId(new UUID(10, 10).randomUUID());
        offer.setImageUrl(imageUtil.saveImage(file));
        UserDTO userDTO = (UserDTO) model.getAttribute("currentUser");
        offer.setSeller(userDTO);
        offerService.register(offer);
        log.info("Created new offer", Level.INFO);
        sessionStatus.setComplete();
        return "redirect:/offers/page/1?seller=" + userDTO.getId();
    }
    @GetMapping("/edit/{id}")
    public String showEditOfferForm(@PathVariable UUID id, Model model, @ModelAttribute OfferDTO offer) {
        Optional<OfferDTO> offerToEdit = offerService.get(id);
        model.addAttribute("offer", offerToEdit.orElse(null));
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("engines", Engine.values());
        model.addAttribute("transmissions", Transmission.values());
        log.info("Open form for update offer " + id, Level.INFO);
        return "edit-offer";
    }
    @PostMapping("/edit")
    public String editOffer(@ModelAttribute @Valid OfferDTO offer,
                            BindingResult bindingResult, Model model, RedirectAttributes re,
                            @RequestParam("file") MultipartFile file, SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            log.info("Offer form has errors", Level.ERROR);
            re.addFlashAttribute("offer", offer);
            re.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/offers/edit/" + offer.getId();
        }
        UserDTO userDTO = (UserDTO) model.getAttribute("currentUser");
        offer.setImageUrl(imageUtil.saveImage(file));
        offer.setSeller(userDTO);
        offerService.update(offer);
        log.info("Updating offer", Level.INFO);
        sessionStatus.setComplete();
        return "redirect:/offers/" + userDTO.getId();
    }
    @GetMapping("/delete/{id}")
    public String deleteOffer(@PathVariable UUID id) {
        log.info("Deleting offer " + id, Level.INFO);
        offerService.delete(id);
        return "redirect:/offers/page/1";
    }
    @GetMapping("/page/{page}")
    public String pageOffers(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "seller", required = false) UUID seller,
                             @RequestParam(value = "contains", required = false) String contains,
                             @PathVariable Integer page, Model model){
        Page<OfferDTO> pageList;
        pageList = offerService.search(page - 1, seller, name, contains);
        model.addAttribute("name", name);
        model.addAttribute("seller", seller);
        model.addAttribute("contains", contains);
        model.addAttribute("offers", pageList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        log.info("Getting offer page " + page, Level.INFO);
        return "page-offers";
    }
    @GetMapping("/{id}")
    public String showOffer(@PathVariable UUID id, Model model) {
        Optional<OfferDTO> offer = offerService.get(id);
        model.addAttribute("offer", offer.orElse(null));
        log.info("Getting offer " + id, Level.INFO);
        return "offer-info";
    }
}