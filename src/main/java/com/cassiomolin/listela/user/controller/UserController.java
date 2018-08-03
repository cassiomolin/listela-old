package com.cassiomolin.listela.user.controller;

import com.cassiomolin.listela.auth.AuthenticatedUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * Create a user.
     *
     * @param createUserDetails
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @NotNull @RequestBody CreateUserDetails createUserDetails) {

        User user = userMapper.toUser(createUserDetails);
        user = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Get a representation of the current authenticated user.
     *
     * @param authenticatedUserDetails
     * @return
     */
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryUserDetails> getUser(@AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        User user = findUser(authenticatedUserDetails.getId() );
        return ResponseEntity.ok(userMapper.toQueryUserDetails(user));
    }

    /**
     * Update details of the current authenticated user.
     *
     * @param updateUserDetails
     * @param authenticatedUserDetails
     * @return
     */
    @PutMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@Valid @NotNull @RequestBody UpdateUserDetails updateUserDetails,
                                           @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        User user = findUser(authenticatedUserDetails.getId());
        userMapper.updateUser(updateUserDetails, user);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    /**
     * Update the password of the current authenticated user.
     *
     * @param updatePasswordDetails
     * @param authenticatedUserDetails
     * @return
     */
    @PutMapping(path = "/me/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(@Valid @NotNull @RequestBody UpdatePasswordDetails updatePasswordDetails,
                                               @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        User user = findUser(authenticatedUserDetails.getId());
        userService.updatePassword(user, updatePasswordDetails.getCurrentPassword(), updatePasswordDetails.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the picture of the current authenticated user.
     *
     * @param authenticatedUserDetails
     * @return
     */
    @GetMapping(path = "/me/picture", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getPicture(@AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        User user = findUser(authenticatedUserDetails.getId());
        if (user.getPicture() == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user.getPicture());
        }
    }

    /**
     * Update the picture of the current authenticated user.
     *
     * @param file
     * @param authenticatedUserDetails
     * @return
     * @throws IOException
     */
    @PutMapping(path = "/me/picture")
    public ResponseEntity<Void> updatePicture(@RequestParam("picture") MultipartFile file,
                                              @AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) throws IOException {

        User user = findUser(authenticatedUserDetails.getId());
        user.setPicture(file.getBytes());
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    /**
     * Remove the picture of the current authenticated user.
     *
     * @param authenticatedUserDetails
     * @return
     */
    @DeleteMapping(path = "/me/picture")
    public ResponseEntity<Void> removePicture(@AuthenticationPrincipal AuthenticatedUserDetails authenticatedUserDetails) {

        User user = findUser(authenticatedUserDetails.getId());
        user.setPicture(null);
        userService.updateUser(user);

        return ResponseEntity.noContent().build();
    }

    private User findUser(String userId) {
        return userService.findUser(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
