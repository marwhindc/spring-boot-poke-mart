package com.pokemartspringboot.services;

import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserNotFoundException;
import com.pokemartspringboot.user.UserRepository;
import com.pokemartspringboot.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindAllUsers() {
        final Long ID = 111L;
        final Long ID2 = 222L;

        User user = new User();
        user.setId(ID);
        User user2 = new User();
        user2.setId(ID2);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> actualUserList = userService.findAll();

        assertThat(actualUserList, hasSize(2));
        assertThat(actualUserList, hasItem(allOf(
                hasProperty("id", is(ID))
        )));
        assertThat(actualUserList, hasItem(allOf(
                hasProperty("id", is(ID2))
        )));
    }

    @Test
    public void testFindUserById() {
        final Long ID = 111L;

        User user = new User();
        user.setId(ID);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        User actualUser = userService.findById(ID);

        assertEquals(ID, actualUser.getId());
    }

    @Test
    public void testFindUserById_userDoesNotExist() {
        final Long ID = 111L;

        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        UserNotFoundException cnfe = assertThrows(UserNotFoundException.class, () -> {
            User actualuser = userService.findById(ID);
        });

        assertEquals("Unable to find user with id: " + ID, cnfe.getMessage());
    }

    @Test
    public void testFindUserByUserName() {
        final String USER_NAME = "aketchum";

        User user = new User();
        user.setUsername(USER_NAME);

        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);

        User actualUser = userService.findByUserName(USER_NAME);

        assertEquals(USER_NAME, actualUser.getUsername());
    }

    @Test
    public void testSaveUser() {
        final Long ID = 111L;
        final String PASSWORD = "test123";

        User user = new User();
        user.setId(ID);
        user.setPassword(PASSWORD);

        when(userRepository.save(user)).thenReturn(user);

        User newUser = userService.save(user);

        assertEquals(ID, newUser.getId());
        assertTrue(new BCryptPasswordEncoder().matches(PASSWORD, newUser.getPassword()));
    }

    @Test
    public void testDeleteUser() {
        final Long ID = 111L;

        User user = new User();
        user.setId(ID);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        userService.delete(ID);

        ArgumentCaptor<User> arg = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).delete(arg.capture());
        User deletedUser = arg.getValue();
        assertEquals(ID, deletedUser.getId());
    }

    @Test
    public void testDeleteUser_userDoesNotExist() {
        final Long ID = 111L;

        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        UserNotFoundException unfe = assertThrows(UserNotFoundException.class, () -> {
            userService.delete(ID);
        });

        assertEquals("Unable to find user with id: " + ID, unfe.getMessage());
    }
}
