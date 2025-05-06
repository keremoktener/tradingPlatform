package com.kerem.controller;

import com.kerem.config.JwtProvider;
import com.kerem.dto.request.UserLoginRequestDTO;
import com.kerem.dto.response.AuthReponse;
import com.kerem.entity.User;
import com.kerem.repository.UserRepository;
import com.kerem.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthReponse> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists:" + user.getEmail());
        }
        User savedUser = userRepository.save(User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthReponse authReponse = AuthReponse.builder()
                .jwt(jwt)
                .status(true)
                .message("User registered successfully")
                .build();


        return ResponseEntity.ok(authReponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthReponse> login(@RequestBody UserLoginRequestDTO dto) {
        String email = dto.email();
        String password = dto.password();
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthReponse authReponse = AuthReponse.builder()
                .jwt(jwt)
                .status(true)
                .message("Logged in successfully")
                .build();


        return ResponseEntity.ok(authReponse);
    }

    private Authentication authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email:" + email);
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }
}
