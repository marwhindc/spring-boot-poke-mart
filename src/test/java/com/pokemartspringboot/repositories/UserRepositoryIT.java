package com.pokemartspringboot.repositories;

import com.pokemartspringboot.cart.CartRepository;
import com.pokemartspringboot.cartitem.CartItemRepository;
import com.pokemartspringboot.product.ProductRepository;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//Repository Integration tests might not be needed as code is provided by Spring. Is as if
//we're testing Spring JPA's code. Created this test for experience

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class UserRepositoryIT {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    //TODO: improve assertion

    @AfterEach
    public void tearDown() throws Exception {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindAll() {
        List<User> userList = userRepository.findAll();

        assertThat(userList, hasSize(2));
    }

    @Test
    public void testFindById() {
        Optional<User> user = userRepository.findById(1L);

        assertNotNull(user);
    }

    @Test
    public void testFindByUserName() {
        User user = userRepository.findByUsername("aketchum");

        assertNotNull(user);
    }

    @Test
    public void testSave() {
        userRepository.deleteAll();

        User user = new User();
        user.setId(3L);
        user.setUsername("jdoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("test123");

        userRepository.save(user);

        Optional<User> createdUser = userRepository.findById(3L);
        assertNotNull(createdUser);
    }

    @Test
    public void testDelete() {
        Optional<User> user = userRepository.findById(1L);
        userRepository.delete(user.get());

        assertFalse(userRepository.findById(1L).isPresent());
    }
}
