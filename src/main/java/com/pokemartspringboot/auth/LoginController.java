package com.pokemartspringboot.auth;

import com.pokemartspringboot.AuthCredentialRequest;
import com.pokemartspringboot.JwtUtil;
import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:5000/"})
//@CrossOrigin(origins = "http://localhost:63765/")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class LoginController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;
    private CartService cartService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentialRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()
                    )
            );

            User user = (User) auth.getPrincipal();
            user.setPassword("");
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(user)
                    )
                    .body(user);
        } catch (BadCredentialsException bce){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token, @AuthenticationPrincipal User user) {
        try {
            Boolean isValid = jwtUtil.validateToken(token, user);
            return ResponseEntity.ok(isValid);
        } catch (ExpiredJwtException eje) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userData) {
        //might move some to service
        User existingUser = userService.findByUserName(userData.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().build();
        }
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setUsername(userData.getUsername());
        user.setPassword(userData.getPassword());
        User createdUser = userService.save(user);

        Cart newCart = new Cart();
        newCart.setUserId(createdUser.getId());
        cartService.save(newCart);

        createdUser.setPassword("");
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtUtil.generateToken(createdUser)
                )
                .body(createdUser);
    }
}

