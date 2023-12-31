package com.example.auto.models;

import com.example.auto.models.base.ExtendedBaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "brands")

public class Brand extends ExtendedBaseEntity {
    protected Brand() {
    }
    private String name;
    private Date created;
    private Date modified;
    private List<Model> models;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getModified() {
        return modified;
    }

    @Override
    public void setModified(Date modified) {
        this.modified = modified;
    }
    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, mappedBy = "brand")
    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
