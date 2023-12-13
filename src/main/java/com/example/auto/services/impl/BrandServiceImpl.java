package com.example.auto.services.impl;

import com.example.auto.dtos.BrandDTO;
import com.example.auto.dtos.ModelDTO;
import com.example.auto.exceptions.EntityIsExistException;
import com.example.auto.exceptions.EntityNotFoundException;
import com.example.auto.models.Brand;
import com.example.auto.repos.BrandRepository;
import com.example.auto.services.BrandService;
import com.example.auto.services.ModelService;
import com.example.auto.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class BrandServiceImpl implements BrandService<BrandDTO> {

    private BrandRepository brandRepository;

    private ModelService modelService;

    private ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public BrandServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }
    @Autowired
    public void setBrandRepository(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Override
    public BrandDTO register(BrandDTO dto) {
        Brand brand = modelMapper.map(dto, Brand.class);
        if (!this.validationUtil.isValid(dto)) {
            return null;
        }
        else if (!brandRepository.findById(brand.getId()).isPresent()){
            brand.setCreated(new Date());
            brand.setModified(new Date());
            return modelMapper.map(brandRepository.saveAndFlush(brand), BrandDTO.class);
        }
        else{
            throw new EntityIsExistException("Brand is already exists.");
        }
    }
    @Cacheable("brands")
    @Override
    public Optional<BrandDTO> get(UUID id) {
        return Optional.ofNullable(modelMapper.map(brandRepository.findById(id), BrandDTO.class));
    }
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Override
    public BrandDTO update(BrandDTO dto) {
         Optional<Brand> brandFromRepository = brandRepository.findById(dto.getId());
        if (!this.validationUtil.isValid(dto)) {
            return null;

        }
        else if (brandFromRepository.isPresent()){
            Brand brand = modelMapper.map(dto, Brand.class);
            brand.setCreated(brandFromRepository.get().getCreated());
            brand.setModified(new Date());
            return modelMapper.map(brandRepository.saveAndFlush(brand), BrandDTO.class);
        }
        else{
            throw new EntityNotFoundException("Brand", dto.getId(), "update");
        }
    }
    @CacheEvict(cacheNames = "brands", allEntries = true)
    @Transactional
    @Override
    public void delete(UUID id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand != null){
            modelService.findByBrand(brand).stream().forEach(s -> {
                ModelDTO m = (ModelDTO) s;
                modelService.delete(m.getId());
            });
            brandRepository.deleteById(id);
        } else{
            throw new EntityNotFoundException("Brand", id, "delete");
        }
    }
    @Cacheable("brands")
    @Override
    public List<BrandDTO> getAll() {
        return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map((s) -> modelMapper.map(s, BrandDTO.class)).collect(Collectors.toList());
    }
}
