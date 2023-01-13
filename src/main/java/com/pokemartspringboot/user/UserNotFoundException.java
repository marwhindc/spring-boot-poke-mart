package com.pokemartspringboot.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("Unable to find user with id: " + id);
    }

    public UserNotFoundException(String username) {
        super("Unable to find user with username: " + username);
    }
}
