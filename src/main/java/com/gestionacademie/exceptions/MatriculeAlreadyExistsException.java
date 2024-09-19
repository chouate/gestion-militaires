package com.gestionacademie.exceptions;

public class MatriculeAlreadyExistsException extends RuntimeException{
    public MatriculeAlreadyExistsException(String message) {
        super(message);
    }
}
