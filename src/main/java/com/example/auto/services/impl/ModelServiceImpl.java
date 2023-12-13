package com.example.auto.services.impl;

import com.example.auto.dtos.ModelDTO;
import com.example.auto.dtos.OfferDTO;
import com.example.auto.exceptions.EntityIsExistException;
import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.models.Brand;
import com.example.auto.models.Model;
import com.example.auto.repos.ModelRepository;
import com.example.auto.services.ModelService;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class ModelServiceImpl implements ModelService<ModelDTO> {
    private final int PAGE_SIZE = 12;
    private ModelRepository modelRepository;

    private ModelMapper modelMapper;

    private OfferService offerService;
    private final ValidationUtil validationUtil;
    @Autowired
    public ModelServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }
    @Autowired
    public void setModelRepository(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Override
    public ModelDTO register(ModelDTO dto) {
        Model model = modelMapper.map(dto, Model.class);
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (!modelRepository.findById(model.getId()).isPresent()){
            model.setCreated(new Date());
            model.setModified(new Date());
            return modelMapper.map(modelRepository.saveAndFlush(model), ModelDTO.class);
        }
        else{
            throw new EntityIsExistException("Model is already exists.");
        }
    }
    @Cacheable("brands")
    @Override
    public Optional<ModelDTO> get(UUID id) {
        return Optional.ofNullable(modelMapper.map(modelRepository.findById(id), ModelDTO.class));
    }
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Override
    public ModelDTO update(ModelDTO dto) {
        Optional<Model> modelFromRepository = modelRepository.findById(dto.getId());
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (modelFromRepository.isPresent()){
            Model model = modelMapper.map(dto, Model.class);
            model.setCreated(modelFromRepository.get().getCreated());
            model.setModified(new Date());
            return modelMapper.map(modelRepository.saveAndFlush(model), ModelDTO.class);
        }
        else{
            throw new EntityNotFoundException("Model", dto.getId(), "update");
        }
    }
    @Transactional
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Override
    public void delete(UUID id) {
        Model model = modelRepository.findById(id).orElse(null);
        if (model != null){
            offerService.findByModel(model).stream().forEach(s -> {
                OfferDTO o = (OfferDTO) s;
                offerService.delete(o.getId());
            });
            modelRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("Model", id, "delete");
        }
    }
    @Cacheable("brands")
    @Override
    public List<ModelDTO> getAll() {
        return modelRepository.findAll(Sort.by(Sort.Direction.ASC, "brand.name", "name")).stream().map((s) -> modelMapper.map(s, ModelDTO.class)).collect(Collectors.toList());
    }
    @Cacheable("brands")
    @Override
    public List<ModelDTO> findByBrand(Brand brand) {
        return modelRepository.findByBrand(brand).stream().map((s) -> modelMapper.map(s, ModelDTO.class)).collect(Collectors.toList());
    }
    @Cacheable("brands")
    public Page<ModelDTO> getPage(Integer pageNumber, Integer pageSize) {
        return modelRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .map(s -> modelMapper.map(s, ModelDTO.class));
    }
    @Cacheable("brands")
    public Page<ModelDTO> getPage(Integer pageNumber) {
        return modelRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .map(s -> modelMapper.map(s, ModelDTO.class));
    }
    public Page<ModelDTO> getPageByBrandName(Integer pageNumber, String name) {
        if(StringUtils.isBlank(name)){
            return modelRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                    .map(s -> modelMapper.map(s, ModelDTO.class));
        } else{
            return modelRepository.findByBrandName(PageRequest.of(pageNumber, PAGE_SIZE), name)
                    .map(s -> modelMapper.map(s, ModelDTO.class));
        }
    }
}
