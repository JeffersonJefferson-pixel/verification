package com.example.verification.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.aspect.RequireCaptcha;
import com.example.verification.captcha.CaptchaService;
import com.example.verification.dto.UserDto;
import com.example.verification.event.OnRegistrationCompleteEvent;
import com.example.verification.exception.ReCaptchaInvalidException;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @Autowired
  private CaptchaService captchaService;


  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @PostMapping("/withCaptcha")
  @RequireCaptcha
  public ResponseEntity<Object> registerUserWithCaptcha(@RequestBody final UserDto userDto, final HttpServletRequest request) throws UserAlreadyExistException {
    Map<String, String> body = new HashMap<>();
    try {

      final UserDto registeredUser = userService.registerNewUser(userDto);
      eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), getAppUrl(request)));

      body.put("message", "User created successfully.");
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (UserAlreadyExistException e) {
      body.put("message", e.getMessage());
      return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

  }

  @PostMapping("")
  public ResponseEntity<Object> registerUser(@RequestBody final UserDto userDto, final HttpServletRequest request)  {
    LOGGER.debug("Registering user: {}", userDto);
    Map<String, String> body = new HashMap<>();

    try {
      final UserDto registeredUser = userService.registerNewUser(userDto);
      eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), getAppUrl(request)));
    } catch (UserAlreadyExistException e) {
      body.put("message", e.getMessage());
      return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    body.put("message", "User created successfully.");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  private String getAppUrl(HttpServletRequest request) {
    return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  }
}
