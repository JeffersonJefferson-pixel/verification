package com.example.verification.email;

import java.io.IOException;

import com.example.verification.dto.UserDto;
import com.example.verification.model.User;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("sendgridService")
public class SendgridService extends AbstractEmailService {

  private final Logger LOGGER = LoggerFactory.getLogger(SendgridService.class);

  private final static String TEMPLATE_FORMAT = "Your OTP: %s";

  public void sendOtpEmail(final UserDto userDto, final String otpToken) throws IOException {
    LOGGER.debug("Sending OTP via email with Mailgun");

    String mailFrom = environment.getProperty("spring.sendgrid.properties.mail.from");
    Email from = new Email(mailFrom);
    String subject = MAIL_SUBJECT;
    Email to = new Email(userDto.getEmail());
    Content content = new Content("text/plain", String.format(TEMPLATE_FORMAT, otpToken));
    Mail mail = new Mail(from, subject, to, content);

    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sendGrid.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
  }
}
