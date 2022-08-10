package com.example.verification.captcha;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.exception.ReCaptchaInvalidException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractCaptchaService implements ICaptchaService {

  private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCaptchaService.class);

  @Autowired
  protected HttpServletRequest request;

  @Autowired
  protected  CaptchaSetting captchaSetting;

//  @Autowired
//  protected ReCaptchaAttemptService reCaptchaAttemptService;

  @Autowired
  protected RestTemplate restTemplate;

  protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");;

  protected static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

  @Override
  public String getReCaptchaSite() {
    return captchaSetting.getSite();
  }

  @Override
  public String getReCaptchaSecret() {
    return captchaSetting.getSecret();
  }

  protected void securityCheck(final String response) throws ReCaptchaInvalidException {
    LOGGER.debug("Validating response {}", response);

//    if (reCaptchaAttemptService.isBlocked(getClientIP())) {
//      throw new ReCaptchaInvalidException("Client exceeded max number of failed attempts");
//    }

    if (!responseSanityCheck(response)) {
      throw new ReCaptchaInvalidException("Response contains invalid characters");
    }
  }

  protected boolean responseSanityCheck(final String response) {
    return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
  }

  protected String getClientIP() {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }
}
