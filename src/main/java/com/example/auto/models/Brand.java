package com.example.auto.models;

import com.example.auto.models.base.ExtendedBaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand extends ExtendedBaseEntity {
    protected Brand() {
    }
    private String name;
    private Date created;
    private Date modified;
    private Set<Model> models;

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
    public Set<Model> getModels() {
        return models;
    }

    public void setModels(Set<Model> models) {
        this.models = models;
    }
}
