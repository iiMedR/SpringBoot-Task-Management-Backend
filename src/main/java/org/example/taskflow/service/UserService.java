package org.example.taskflow.service;

import org.example.taskflow.dto.UserResponse;
import org.example.taskflow.exception.EmailAlreadyExistsException;
import org.example.taskflow.exception.UserNotFoundException;
import org.example.taskflow.model.User;
import org.example.taskflow.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Authenticated user was not found"
                        )
                );
    }

    @Transactional(readOnly = true)
    public void demonstrateNPlusOne() {
        List<User> users = userRepository.findAllWithTasks();
        System.out.println("Users loaded");

        for(User user : users) {
            System.out.println(
                    user.getEmail()
                    + " has"
                    + user.getTasks().size()
                    + " tasks"
            );
        }
    }
}
