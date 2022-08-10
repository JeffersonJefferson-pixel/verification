package com.example.verification.email;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.example.verification.dto.UserDto;
import com.example.verification.model.User;

public interface IEmailService {
  void sendOtpEmail(final UserDto userDto, final String otpToken) throws MessagingException, UnsupportedEncodingException, IOException;
}
