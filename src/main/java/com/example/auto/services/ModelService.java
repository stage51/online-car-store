package com.example.auto.services;
import com.example.auto.models.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ModelService<ModelDTO> extends AbstractService<ModelDTO>{
    List<ModelDTO> findByBrand(Brand brand);
    Page<ModelDTO> getPage(Integer pageNumber, Integer pageSize);
    Page<ModelDTO> getPage(Integer pageNumber);
    Page<ModelDTO> getPageByBrandName(Integer pageNumber, String name);
}
