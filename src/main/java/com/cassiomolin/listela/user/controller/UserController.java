package com.cassiomolin.listela.user.controller;

import com.cassiomolin.listela.user.controller.mapper.UserMapper;
import com.cassiomolin.listela.user.controller.model.CreateUserDetails;
import com.cassiomolin.listela.user.controller.model.QueryUserDetails;
import com.cassiomolin.listela.user.controller.model.UpdatePasswordDetails;
import com.cassiomolin.listela.user.controller.model.UpdateUserDetails;
import com.cassiomolin.listela.user.domain.User;
import com.cassiomolin.listela.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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


    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryUserDetails> getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(userMapper.toQueryUserDetails(user));
    }

    @PutMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@Valid @NotNull @RequestBody UpdateUserDetails updateUserDetails) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userMapper.updateUser(updateUserDetails, user);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/me/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(@Valid @NotNull @RequestBody UpdatePasswordDetails updatePasswordDetails) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userService.updatePassword(user, updatePasswordDetails.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/me/picture", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<byte[]> getPicture() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(user.getPicture());
    }

    @PutMapping(path = "/me/picture", consumes  = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<Void> updatePicture(@RequestParam("picture") MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setPicture(file.getBytes());
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/me/picture")
    public ResponseEntity<Void> removePicture() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setPicture(null);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }
}
