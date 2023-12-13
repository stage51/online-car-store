package com.example.auto.dtos;

import com.example.auto.models.Brand;
import com.example.auto.models.Offer;
import com.example.auto.models.enums.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter

public class ModelDTO extends BaseDTO{
    private UUID id;
    private String name;
    private Category category;
    private String imageUrl;
    private int startYear;
    private int endYear;
    private BrandDTO brand;
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Name length must be more than two characters!")
    public String getName() {
        return name;
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
