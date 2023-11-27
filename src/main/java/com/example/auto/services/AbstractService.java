package com.example.auto.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AbstractService<T> {
    T register(T dto);
    Optional<T> get(UUID id);
    T update(T dto);
    void delete(UUID id);
    List<T> getAll();
}
