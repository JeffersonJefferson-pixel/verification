package com.example.verification.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import com.example.verification.dto.UserDto;
import com.example.verification.email.IEmailService;
import com.example.verification.exception.UserNotFoundException;
import com.example.verification.model.Otp;
import com.example.verification.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OtpController {

  @Autowired
  private OtpService otpService;

  @Autowired
  private IEmailService mailgunService;

  @PostMapping("")
  public ResponseEntity<Object> sendOtp(@RequestBody UserDto userDto) {
    Map<String, String> body = new HashMap<>();
    try {
      Otp otp = otpService.generateOtpForUser(userDto);
      mailgunService.sendOtpEmail(userDto, otp.getToken());

      body.put("message", "Otp sent.");
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (UserNotFoundException e) {
      body.put("message", e.getMessage());
      return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    } catch (MessagingException | IOException e) {
      e.printStackTrace();
      body.put("message", "Unable to send email.");
      return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

}
