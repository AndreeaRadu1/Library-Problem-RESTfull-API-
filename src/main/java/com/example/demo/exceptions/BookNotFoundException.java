package com.example.demo.exceptions;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(long id){
        super(String.format("Invalid book id: " + id + "!"));
    }
}
