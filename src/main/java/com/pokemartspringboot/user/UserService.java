package com.pokemartspringboot.user;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void delete(Long id);
    User findByUserName(String userName);
}
