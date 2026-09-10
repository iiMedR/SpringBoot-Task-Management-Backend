package org.example.taskflow.controller;

import jakarta.validation.Valid;
import org.example.taskflow.dto.CreateUserRequest;
import org.example.taskflow.dto.LoginRequest;
import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.model.User;
import org.example.taskflow.service.JwtService;
import org.example.taskflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,  JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        /*SecurityContextHolder.getContext()
                .setAuthentication(authentication);*/

        String token =
                jwtService.generateToken(authentication.getName());

        return token;
    }

}
