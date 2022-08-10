package com.example.verification.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.verification.dto.UserDto;
import com.example.verification.event.RegistrationListener;
import com.example.verification.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service("mailgunService")
public class MailgunService extends AbstractEmailService {

  private final Logger LOGGER = LoggerFactory.getLogger(MailgunService.class);

  @Override
  public void sendOtpEmail(final UserDto userDto, final String otpToken) throws MessagingException, UnsupportedEncodingException {
    LOGGER.debug("Sending OTP via email with Mailgun");

    String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
    String mailFromName = environment.getProperty("mail.from.name", "Identity");

    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper email;
    email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    email.setTo(userDto.getEmail());
    email.setSubject(MAIL_SUBJECT);
    email.setFrom(new InternetAddress(mailFrom, mailFromName));

    final Context ctx = new Context(LocaleContextHolder.getLocale());
    ctx.setVariable("email", userDto.getEmail());
    ctx.setVariable("otp", otpToken);

    final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

    email.setText(htmlContent, true);

    ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

    email.addInline("springLogo", clr, PNG_MIME);

    mailSender.send(mimeMessage);
  }
}
