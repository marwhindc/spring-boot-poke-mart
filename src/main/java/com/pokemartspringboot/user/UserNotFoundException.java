package com.pokemartspringboot.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("Unable to find user with id: " + id);
    }
}
