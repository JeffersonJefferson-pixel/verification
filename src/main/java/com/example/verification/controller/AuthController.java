package com.example.verification.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.verification.dto.OneTimePasswordDto;
import com.example.verification.exception.UserNotFoundException;
import com.example.verification.security.jwt.JWTToken;
import com.example.verification.security.jwt.TokenProvider;
import com.example.verification.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  @Autowired
  private OtpService otpService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping(value = "verification")
  public ResponseEntity<JWTToken> verifyOtp(@RequestBody OneTimePasswordDto otpDto) throws UserNotFoundException {
    String otpToken = otpDto.getToken();
    String username = otpDto.getUser().getUsername();
    Map<String, String> body = new HashMap<>();

    // assuming otp is deleted if user is deleted
    // a valid otp will have a user
    // this assumes username in request body is valid
    final boolean isValid = otpService.verifyOtp(otpToken);

    if (isValid) {
      // TODO: send Jwt when OTP is valid
      // this won't throw UserNotFoundException
      String jwt = tokenProvider.createTokenAfterVerifiedOtp(username);
      JWTToken response = new JWTToken(jwt);
      body.put("message", "User verification succeeds.");
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      body.put("message", "User verification fails.");
      return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
