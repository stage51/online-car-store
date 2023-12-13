package com.example.auto.services.impl;

import com.example.auto.dtos.OfferDTO;
import com.example.auto.dtos.UserDTO;
import com.example.auto.exceptions.EntityIsExistException;
import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.models.Model;
import com.example.auto.models.Offer;
import com.example.auto.models.User;
import com.example.auto.repos.OfferRepository;
import com.example.auto.services.OfferService;
import com.example.auto.utils.ValidationUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class OfferServiceImpl implements OfferService<OfferDTO> {
    private final int PAGE_SIZE = 12;
    private final int LATEST_SIZE = 4;
    private OfferRepository offerRepository;

    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;

    @Autowired
    public OfferServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setOfferRepository(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }
    @CacheEvict(cacheNames = "offers", allEntries = true)
    @Override
    public OfferDTO register(OfferDTO dto) {
        Offer offer = modelMapper.map(dto, Offer.class);
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (!offerRepository.findById(offer.getId()).isPresent()){
            offer.setModified(new Date());
            offer.setCreated(new Date());
            return modelMapper.map(offerRepository.saveAndFlush(offer), OfferDTO.class);
        }
        else{
            throw new EntityIsExistException("Offer is already exists.");
        }
    }
    @Cacheable("offers")
    @Override
    public Optional<OfferDTO> get(UUID id) {
        return Optional.ofNullable(modelMapper.map(offerRepository.findById(id), OfferDTO.class));
    }
    @CacheEvict(cacheNames = "offers", allEntries = true)
    @Override
    public OfferDTO update(OfferDTO dto) {
        Optional<Offer> offerFromRepository = offerRepository.findById(dto.getId());
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (offerFromRepository.isPresent()){
            Offer offer = modelMapper.map(dto, Offer.class);
            offer.setCreated(offerFromRepository.get().getCreated());
            offer.setModified(new Date());
            return modelMapper.map(offerRepository.saveAndFlush(offer), OfferDTO.class);
        }
        else{
            throw new EntityNotFoundException("Offer", dto.getId(), "update");
        }
    }
    @CacheEvict(cacheNames = "offers", allEntries = true)
    @Override
    public void delete(UUID id) {
        if (offerRepository.findById(id).isPresent()){
            offerRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("Offer", id, "delete");
        }
    }
    @CacheEvict(cacheNames = "offers", allEntries = true)
    @Override
    public List<OfferDTO> getAll() {
        return offerRepository.findAll().stream().map((s) -> modelMapper.map(s, OfferDTO.class)).collect(Collectors.toList());
    }
    @Cacheable("offers")
    @Override
    public List<OfferDTO> findByModel(Model model) {
        return offerRepository.findByModel(model).stream().map((s) -> modelMapper.map(s, OfferDTO.class)).collect(Collectors.toList());
    }
    @Cacheable("offers")
    @Override
    public Page<OfferDTO> getPage(Integer pageNumber, Integer pageSize) {
        return offerRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .map((s) -> modelMapper.map(s, OfferDTO.class));
    }
    @Cacheable("offers")
    @Override
    public Page<OfferDTO> getPage(Integer pageNumber) {
        return offerRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .map((s) -> modelMapper.map(s, OfferDTO.class));
    }
    @Override
    public Page<OfferDTO> getPageByModelName(Integer pageNumber, String name) {
        if (StringUtils.isBlank(name)){
            return getPage(pageNumber);
        }
        return offerRepository.findByModelName(PageRequest.of(pageNumber, PAGE_SIZE), name)
                .map((s) -> modelMapper.map(s, OfferDTO.class));
    }
    @Override
    public Page<OfferDTO> getPageBySeller(Integer pageNumber, UUID seller) {
        if (seller == null){
            return getPage(pageNumber);
        }
        return offerRepository.findBySellerId(PageRequest.of(pageNumber, PAGE_SIZE), seller)
                .map((s) -> modelMapper.map(s, OfferDTO.class));
    }

    @Override
    public Page<OfferDTO> getPageBySellerAndModelName(Integer pageNumber, UUID seller, String name) {
        if (StringUtils.isBlank(name)){
            return getPageBySeller(pageNumber, seller);
        }
        else if (seller == null){
            return getPageByModelName(pageNumber, name);
        }
        else{
            return offerRepository.findBySellerIdAndModelName(PageRequest.of(pageNumber, PAGE_SIZE), seller, name)
                    .map((s) -> modelMapper.map(s, OfferDTO.class));
        }
    }
    @Override
    public Page<OfferDTO> search(Integer pageNumber, UUID seller, String name, String contains) {
        if ((seller != null) || (StringUtils.isNotBlank(name))){
            return getPageBySellerAndModelName(pageNumber, seller, name);
        }
        else if(StringUtils.isNotBlank(contains)){
            return offerRepository.findByModelNameContainsOrDescriptionContainsOrModelBrandNameContains(PageRequest.of(pageNumber, PAGE_SIZE),
                            contains, contains, contains)
                    .map((s) -> modelMapper.map(s, OfferDTO.class));
        }
        else{
            return getPage(pageNumber);
        }
    }
    @Cacheable("offers")
    @Override
    public List<OfferDTO> latestOffers() {
        return offerRepository.findAll(PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "created")))
                .map((s) -> modelMapper.map(s, OfferDTO.class)).stream().collect(Collectors.toList());
    }
}
