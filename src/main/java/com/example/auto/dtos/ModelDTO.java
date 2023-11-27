package com.example.auto.dtos;

import com.example.auto.models.Brand;
import com.example.auto.models.Offer;
import com.example.auto.models.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    private Set<OfferDTO> offers;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
