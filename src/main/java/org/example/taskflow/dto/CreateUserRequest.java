package org.example.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 25, message = "name must not exceed 25 characters")
    private String name;

    @NotBlank
    private String email;

    public CreateUserRequest() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
