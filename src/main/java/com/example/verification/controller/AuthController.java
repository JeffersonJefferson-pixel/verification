package com.example.verification.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.verification.dto.OtpDto;
import com.example.verification.exception.OtpInvalidException;
import com.example.verification.model.Otp;
import com.example.verification.security.jwt.JwtService;
import com.example.verification.security.jwt.JwtToken;
//import com.example.verification.security.jwt.TokenProvider;
import com.example.verification.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private OtpService otpService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("")
  public ResponseEntity<JwtToken> verifyOtp(@RequestBody final OtpDto otpDto) {
    Map<String, String> body = new HashMap<>();

    // assuming otp is deleted if user is deleted
    // a valid otp will have a user
    // this assumes username in request body is valid
    try {
      final Otp otp = otpService.verifyOtp(otpDto.getToken());
        // TODO: send Jwt when OTP is valid
        // this won't throw UserNotFoundException
//      String jwt = tokenProvider.createTokenAfterVerifiedOtp(username);
        String jwt = jwtService.generateToken(otp.getUser().getEmail());
        JwtToken response = new JwtToken(jwt);
        body.put("message", "User verification succeeds.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (OtpInvalidException e) {
      body.put("message", e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
