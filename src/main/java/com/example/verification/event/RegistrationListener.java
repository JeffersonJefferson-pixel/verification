package com.example.verification.event;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.verification.model.OneTimePassword;
import com.example.verification.model.User;
import com.example.verification.service.EmailService;
import com.example.verification.service.OtpService;
import com.example.verification.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
  @Autowired
  private UserService userService;

  @Autowired
  private OtpService otpService;

  @Autowired EmailService emailService;

  @SneakyThrows
  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
    System.out.println("Event caught");
    User user = event.getUser();

    OneTimePassword otp = otpService.generateOtpForUser(user);
    System.out.println("otp created");

//    emailService.sendOtpEmail(user, otp.getToken());
//    System.out.println("Email sent");
  }
}
