package com.example.auto.repos;

import com.example.auto.models.Model;
import com.example.auto.models.Offer;
import com.example.auto.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByModel (Model model);
    Page<Offer> findByModelName (Pageable pageable, String name);
    Page<Offer> findAll(Pageable pageable);
    Page<Offer> findBySellerId(Pageable pageable, UUID seller);
    Page<Offer> findBySellerIdAndModelName(Pageable pageable, UUID seller, String name);
    Page<Offer> findByModelNameContainsOrDescriptionContainsOrModelBrandNameContains
            (Pageable pageable, String modelNameContains, String descriptionContains, String modelBrandNameContains);
}
