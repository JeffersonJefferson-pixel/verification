package com.example.verification.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.dto.UserDto;
import com.example.verification.event.OnRegistrationCompleteEvent;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.model.OneTimePassword;
import com.example.verification.model.User;
import com.example.verification.service.OtpService;
import com.example.verification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class RegistrationController {
  @Autowired
  private UserService userService;

  @Autowired
  private OtpService otpService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @PostMapping("/registration")
  public ResponseEntity<Object> registerUser(@RequestBody final UserDto userDto, final HttpServletRequest request) throws UserAlreadyExistException {
    System.out.println("in route");
    final User registeredUser = userService.registerNewUser(userDto);
    System.out.println("user created");
    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), getAppUrl(request)));
    System.out.println("event handled");
    Map<String, String> body = new HashMap<>();
    body.put("message", "User created successfully.");

    return new ResponseEntity<>(body, HttpStatus.OK);
  }

//  @GetMapping("/verification")
//  public ResponseEntity<Object> verify(@RequestParam("token") final String token, final HttpServletRequest request) {
//    Locale locale = request.getLocale();
//    Map<String, String> body = new HashMap<>();
//
//
//    final boolean isValid = otpService.verifyOtp(token);
//
//    if (isValid) {
//      // TODO: send Jwt when OTP is valid
//      body.put("message", "User verification succeeds.");
//      return new ResponseEntity<>(body, HttpStatus.OK);
//    } else {
//      body.put("message", "User verification fails.");
//      return  new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//  }

//  @GetMapping("/registrationConfirm")
//  public String confirmRegistration(WebRequest request, Model model, @RequestParam("otp") String otp) {
//    Locale locale = request.getLocale();
//
//    OneTimePassword foundOtp = userService.getOtp(otp);
//    if (otp == null) {
//      String message = messages.getMessage("auth.message.invalidToken", null, locale);
//      model.addAttribute("message", message);
//      return "redirect:/badUser.html?lang=" + locale.getLanguage();
//    }
//
//    User user = foundOtp.getUser();
//    Calendar cal = Calendar.getInstance();
//    if ((foundOtp.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//      String messageValue = messages.getMessage("auth.message.expired", null, locale);
//      model.addAttribute("message", messageValue);
//      return "redirect:/badUser.html?lang=" + locale.getLanguage();
//    }
//
//    user.setEnabled(true);
//    userService.saveRegisteredUser(user);
//    return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
//  }

  private String getAppUrl(HttpServletRequest request) {
    return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  }
}
