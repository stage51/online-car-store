package com.example.auto.dtos;

import com.example.auto.models.Model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Getter
@Setter

public class BrandDTO extends BaseDTO{
    private String name;
    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Name length must be more than two characters!")
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
