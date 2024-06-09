package com.pokemartspringboot.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    public User findByUserName(String username) {
        //not working with MyUserDetailsService
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            return user;
//        } else throw new UserNotFoundException(username);
        return userRepository.findByUsername(username);
    }
}
