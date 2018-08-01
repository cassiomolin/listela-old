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
import java.security.Principal;

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
    public ResponseEntity<QueryUserDetails> getUser(Principal principal) {

        User user = findUser(principal);
        return ResponseEntity.ok(userMapper.toQueryUserDetails(user));
    }

    @PutMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@Valid @NotNull @RequestBody UpdateUserDetails updateUserDetails,
                                           Principal principal) {

        User user = findUser(principal);
        userMapper.updateUser(updateUserDetails, user);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/me/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(@Valid @NotNull @RequestBody UpdatePasswordDetails updatePasswordDetails,
                                               Principal principal) {

        User user = findUser(principal);
        userService.updatePassword(user, updatePasswordDetails.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/me/picture", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getPicture(Principal principal) {

        User user = findUser(principal);
        if (user.getPicture() == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user.getPicture());
        }
    }

    @PutMapping(path = "/me/picture")
    public ResponseEntity<Void> updatePicture(@RequestParam("picture") MultipartFile file,
                                              Principal principal) throws IOException {

        User user = findUser(principal);
        user.setPicture(file.getBytes());
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/me/picture")
    public ResponseEntity<Void> removePicture(Principal principal) {

        User user = findUser(principal);
        user.setPicture(null);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    private User findUser(Principal principal) {
        return userService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
