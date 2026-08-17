package org.example.taskflow.service;

import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.exception.UserNotFoundException;
import org.example.taskflow.model.User;
import org.example.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse createUser(String name, String email){
        User user = new User();

        user.setName(name);
        user.setEmail(email);

        userRepository.save(user);

        return toResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return toResponse(user);
    }

    public void deleteUserById(Long id) {

        userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        userRepository.deleteById(id);
    }
}
