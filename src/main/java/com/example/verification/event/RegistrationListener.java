package com.example.verification.event;

import java.io.IOException;

import javax.mail.MessagingException;

import com.example.verification.dto.UserDto;
import com.example.verification.email.IEmailService;
import com.example.verification.exception.UserNotFoundException;
import com.example.verification.model.Otp;
import com.example.verification.service.OtpService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  @Autowired
  private OtpService otpService;

  @Autowired
  private IEmailService sendgridService;

  @SneakyThrows
  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.sendOtp(event);
  }

  private void sendOtp(OnRegistrationCompleteEvent event) throws UserNotFoundException, MessagingException, IOException {
    UserDto userDto = event.getUserDto();

    Otp otp = otpService.generateOtpForUser(userDto);

    sendgridService.sendOtpEmail(userDto, otp.getToken());
  }
}
