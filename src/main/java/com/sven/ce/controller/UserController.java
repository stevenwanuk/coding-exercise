package com.sven.ce.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.ce.controller.model.RegisterUserRequest;
import com.sven.ce.model.User;
import com.sven.ce.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/*
 * Api description is deprecated, but there is no alternative left.
 */
@Api(description = "User Service Apis")
@RestController
@RequestMapping("users")
public class UserController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @ApiOperation(value = "Register a new User")
    @PostMapping(value = { "/", "/register" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody @Valid RegisterUserRequest registerUserRequest)
    {

        // TODO might be aGDPR issue
        LOGGER.info("Received register({})  http request", registerUserRequest);

        User user = User.builder()
                        .username(registerUserRequest.getUsername())
                        .password(registerUserRequest.getPassword())
                        .ssn(registerUserRequest.getSsn())
                        .dateOfBirth(registerUserRequest.getDateOfBirth())
                        .build();

        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @ApiOperation(value = "Get user details by giving userId")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Long userId)
    {

        LOGGER.info("Received getUserById({})  http request", userId);

        return userService.getUserById(userId);
    }

    @ApiOperation(value = "List all users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers()
    {
        LOGGER.info("Received getAllUsers()  http request");

        return userService.getAllUsers();
    }
}
