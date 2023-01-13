package com.pokemartspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.UserDetailsServiceImpl;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserController;
import com.pokemartspringboot.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private CartService cartService;
    @MockBean
    private ProductService productService;
    @MockBean
    private CartItemService cartItemService;

    private final long ID = 111;

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUsers() throws Exception {
        User user = new User();
        user.setId(ID);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(ID);

        when(userService.findById(ID)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUserByUserName() throws Exception {
        final String USERNAME = "jdoe";

        User user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);

        when(userService.findByUserName(USERNAME)).thenReturn(user);

        mockMvc.perform(get("/api/users/username/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setId(ID);
        user.setUsername("jdoe");
        user.setPassword("test123");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.save(user)).thenReturn(user);

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", ID))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(userService, times(1)).delete(ID);
    }
}
