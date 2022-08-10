package com.example.verification.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @PostMapping("")
  public ResponseEntity<Object> createUser(@RequestBody final UserDto userDto)  {
    LOGGER.debug("Creating user: {}", userDto);
    Map<String, String> body = new HashMap<>();

    try {
      userService.registerNewUser(userDto);
    } catch (UserAlreadyExistException e) {
      body.put("message", e.getMessage());
      return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    body.put("message", "User created successfully.");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

}
