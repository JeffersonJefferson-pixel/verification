package com.example.verification.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.example.verification.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class EmailService {
  private static final String TEMPLATE_NAME = "registration";
  private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
  private static final String PNG_MIME = "image/png";
  private static final String MAIL_SUBJECT = "Registration Confirmation";

  @Autowired
  private MessageSource messages;

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private Environment environment;

  @Autowired
  private TemplateEngine htmlTemplateEngine;

  public void sendOtpEmail(final User user, final String token) throws MessagingException, UnsupportedEncodingException {
    String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
    String mailFromName = environment.getProperty("mail.from.name", "Identity");

    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper email;
    email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    email.setTo(user.getEmail());
    email.setSubject(MAIL_SUBJECT);
    email.setFrom(new InternetAddress(mailFrom, mailFromName));

    final Context ctx = new Context(LocaleContextHolder.getLocale());
    ctx.setVariable("email", user.getEmail());
    ctx.setVariable("username", user.getUsername());
    ctx.setVariable("otp", token);

    final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

    email.setText(htmlContent, true);

    ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

    email.addInline("springLogo", clr, PNG_MIME);

    mailSender.send(mimeMessage);
  }
}
