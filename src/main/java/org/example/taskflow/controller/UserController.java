package org.example.taskflow.controller;

import jakarta.validation.Valid;
import org.example.taskflow.dto.CreateUserRequest;
import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.model.User;
import org.example.taskflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest user) {
        String email = user.getEmail();
        String name = user.getName();
        return userService.createUser(name, email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
