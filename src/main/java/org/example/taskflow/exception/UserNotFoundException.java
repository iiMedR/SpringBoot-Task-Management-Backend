package org.example.taskflow.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {super("User with ID " + id + " was not found");}
}
