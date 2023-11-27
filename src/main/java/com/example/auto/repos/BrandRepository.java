package com.example.auto.repos;

import com.example.auto.models.Brand;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    List<Brand> findAll(Sort sort);
}
