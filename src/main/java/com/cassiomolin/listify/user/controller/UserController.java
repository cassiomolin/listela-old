package com.cassiomolin.listify.user.controller;

import com.cassiomolin.listify.user.controller.mapper.UserMapper;
import com.cassiomolin.listify.user.controller.model.CreateUserDetails;
import com.cassiomolin.listify.user.controller.model.QueryUserDetails;
import com.cassiomolin.listify.user.domain.User;
import com.cassiomolin.listify.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @NotNull @RequestBody CreateUserDetails createUserDetails) {

        User user = userMapper.toUser(createUserDetails);
        user = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryUserDetails> findUser(@PathVariable String id) {
        User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(userMapper.toQueryUserDetails(user));
    }

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryUserDetails> getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            QueryUserDetails userDetails = new QueryUserDetails();
            userDetails.setEmail(authentication.getName());
            return ResponseEntity.ok(userDetails);
        }

        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        QueryUserDetails userDetails = userMapper.toQueryUserDetails(user);
        return ResponseEntity.ok(userDetails);
    }
}
