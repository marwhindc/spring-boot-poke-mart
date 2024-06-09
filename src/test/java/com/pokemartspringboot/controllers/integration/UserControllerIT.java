package com.pokemartspringboot.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.product.ProductRepository;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserRepository;
import com.pokemartspringboot.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    public void tearDown() throws Exception {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUsers() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/users"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUserById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/users/{id}", 1));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("aketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUserByUserName() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/users/username/{username}", "aketchum"));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("aketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateUser() throws Exception {
        //given
        User user = new User();
        user.setUsername("jdoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("test123");

        //when
        ResultActions response =  mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        //then
        response.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteUser() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/users/{id}", 1));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
