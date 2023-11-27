package com.example.auto.services;

import com.example.auto.dtos.UserDTO;
import com.example.auto.models.Model;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OfferService<OfferDTO> extends AbstractService<OfferDTO>{
    List<OfferDTO> findByModel(Model model);
    Page<OfferDTO> getPage(Integer pageNumber, Integer pageSize);
    Page<OfferDTO> getPage(Integer pageNumber);
    Page<OfferDTO> getPageByModelName(Integer pageNumber, String name);
    Page<OfferDTO> getPageBySeller(Integer pageNumber, UUID seller);
    Page<OfferDTO> getPageBySellerAndModelName(Integer pageNumber, UUID seller, String name);
    Page<OfferDTO> search(Integer pageNumber, UUID seller, String name, String contains);
}
