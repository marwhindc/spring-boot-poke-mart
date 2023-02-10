package com.pokemartspringboot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public User findByUserName(String username) {
        //not working with MyUserDetailsService
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            return user;
//        } else throw new UserNotFoundException(username);
        return userRepository.findByUsername(username);
    }
}
