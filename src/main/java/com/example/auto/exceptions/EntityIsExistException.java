package com.example.auto.exceptions;

public class EntityIsExistException extends RuntimeException{
    public EntityIsExistException(String m){
        super(m);
    }
}
