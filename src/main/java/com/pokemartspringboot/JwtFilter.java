package com.pokemartspringboot;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pokemartspringboot.user.UserService;

import static org.thymeleaf.util.StringUtils.isEmpty;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ((isEmpty(header) || !header.startsWith("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();

        //Get user identity and set it on the spring securty context
        UserDetails userDetails = userService.findByUserName(jwtUtil.getUserNameFromToken(token));

        //Get jwt token and validate
        if (!jwtUtil.validateToken(token, userDetails)) {
            filterChain.doFilter(request, response);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails == null ? List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
