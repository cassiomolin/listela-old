package com.cassiomolin.listela.user.service;

import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {

        findUserByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("User already exists with this email");
        });

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // TODO: Send email

        return userRepository.insert(user);
    }

    public Optional<User> findUser(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User updatePassword(User user, String currentPassword, String newPassword) {

        if (!currentPassword.equals(newPassword)) {
            throw new RuntimeException("Passwords don't match"); // FIXME
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password doesn't match"); // FIXME
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
