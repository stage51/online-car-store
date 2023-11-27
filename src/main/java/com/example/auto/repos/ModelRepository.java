package com.example.auto.repos;

import com.example.auto.models.Brand;
import com.example.auto.models.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ModelRepository extends JpaRepository<Model, UUID> {
    List<Model> findByBrand(Brand brand);
    List<Model> findAll(Sort sort);
    Page<Model> findAll(Pageable pageable);
    Page<Model> findByBrandName(Pageable pageable, String name);
}
