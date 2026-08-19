package org.example.taskflow.service;

import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.exception.EmailAlreadyExistsException;
import org.example.taskflow.exception.UserNotFoundException;
import org.example.taskflow.model.User;
import org.example.taskflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse createUser(String name, String email, String password) {
        User user = new User();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        user.setName(name);
        user.setEmail(email);

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

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
