package com.example.verification.email;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

public abstract class AbstractEmailService implements IEmailService {

  protected static final String TEMPLATE_NAME = "registration";
  protected static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
  protected static final String PNG_MIME = "image/png";
  protected static final String MAIL_SUBJECT = "Registration Confirmation";

  @Autowired
  protected MessageSource messages;

  @Autowired
  protected JavaMailSender mailSender;

  @Autowired
  SendGrid sendGrid;

  @Autowired
  protected Environment environment;

  @Autowired
  protected TemplateEngine htmlTemplateEngine;
}
