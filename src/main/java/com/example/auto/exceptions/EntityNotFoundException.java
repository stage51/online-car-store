package com.example.auto.exceptions;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entity, UUID id, String action){
        super(entity + " " + id + "is not found. Action: " + action + ".");
    }
    public EntityNotFoundException(String entity, String name){
        super(entity + " with name " + name + " is not found.");
    }
}
