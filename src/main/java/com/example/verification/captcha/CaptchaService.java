package com.example.verification.captcha;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.exception.ReCaptchaInvalidException;
import com.example.verification.exception.ReCaptchaUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

@Service
public class CaptchaService extends AbstractCaptchaService {

  private final Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);

  @Override
  public void processRequest(final HttpServletRequest request) throws ReCaptchaInvalidException {
    LOGGER.debug("Processing captcha");
    final String response = request.getParameter("g-recaptcha-response");
    securityCheck(response);

    MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
    requestMap.add("secret", getReCaptchaSecret());
    requestMap.add("response", response);
    requestMap.add("remoteip", getClientIP());

    try {
      final CaptchaResponse captchaResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap, CaptchaResponse.class);
      LOGGER.debug("Google's response: {}", captchaResponse.toString());

      // fail
      if (!captchaResponse.isSuccess()) {
//        reCaptchaAttemptService.reCaptchaFailed(getClientIP());
        throw new ReCaptchaInvalidException("reCaptcha is invalid");
      }

      // succeeds
//      reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    } catch (RestClientException rce) {
      throw new ReCaptchaUnavailableException("Recaptcha unavailable at this time.  Please try again later.", rce);
    }
  }
}
