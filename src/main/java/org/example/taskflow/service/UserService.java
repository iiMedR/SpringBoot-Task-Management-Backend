package org.example.taskflow.service;

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

    public User createUser(String name, String email){
        User user = new User();

        user.setName(name);
        user.setEmail(email);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
    }
}
