package com.example.verification.service;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.model.OneTimePassword;
import com.example.verification.model.User;
import com.example.verification.repository.OneTimePasswordRepository;
import com.example.verification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Transactional
public class UserService {
//  private static final String TEMPLATE_NAME = "registration";
//  private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
//  private static final String PNG_MIME = "image/png";
//  private static final String MAIL_SUBJECT = "Registration Confirmation";

//  private final Environment environment;
//
//  private final JavaMailSender mailSender;
//
//  private final TemplateEngine htmlTemplateEngine;

  @Autowired
  private UserRepository userRepository;

  public static final String TOKEN_INVALID = "invalidToken";
  public static final String TOKEN_EXPIRED = "expired";
  public static final String TOKEN_VALID = "valid";

//  public UserService(Environment environment, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
//    this.environment = environment;
//    this.mailSender = mailSender;
//    this.htmlTemplateEngine = htmlTemplateEngine;
//  }

  public User registerNewUser(final UserDto userDto) throws UserAlreadyExistException {
    if (emailExists(userDto.getEmail())) {
      throw new UserAlreadyExistException("User already exists");
    }

    final User user = new User();

    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());

    return userRepository.save(user);
  }

  private boolean emailExists(final String email) {
    return userRepository.findByEmail(email) != null;
  }

//  public OneTimePassword getOtp(String otp) {
//    return oneTimePasswordRepository.findByPassword(otp);
//  }

//  public void clearOTP(User user) {
//    user.setOneTimePassword(null);
//    user.setOtpRequestedTime(null);
//    userRepository.save(user);
//  }
}