package com.cassiomolin.listify.user.service;

import com.cassiomolin.listify.user.domain.User;
import com.cassiomolin.listify.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.insert(user);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}
