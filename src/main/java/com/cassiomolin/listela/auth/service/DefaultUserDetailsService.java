package com.cassiomolin.listela.auth.service;

import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
import com.cassiomolin.listela.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));

        return new AuthenticatedUserDetails.Builder()
                .withId(user.getId())
                .withUsername(user.getEmail())
                .withPassword(user.getPassword())
                .withAuthorities(Collections.emptySet())
                .withActive(user.isActive())
                .build();
    }
}