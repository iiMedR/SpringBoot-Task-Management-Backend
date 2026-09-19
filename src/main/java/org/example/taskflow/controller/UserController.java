package org.example.taskflow.controller;

import jakarta.validation.Valid;
import org.example.taskflow.dto.CreateUserRequest;
import org.example.taskflow.dto.TaskResponse;
import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.model.User;
import org.example.taskflow.service.TaskService;
import org.example.taskflow.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserById() {

        User user = userService.getCurrentUser();

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/tasks")
    public Page<TaskResponse> getTasksByUserId(@PathVariable Long userId, Pageable pageable) {
        return taskService.getTasksByUserId(userId, pageable);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserById() {
        User user = userService.getCurrentUser();
        userService.deleteUserById(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/n-plus-one-test")
        public ResponseEntity<Void> testNPlusOne() {
        userService.demonstrateNPlusOne();
        return ResponseEntity.ok().build();
    }
}
