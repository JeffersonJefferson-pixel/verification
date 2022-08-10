package com.example.verification.captcha;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.exception.ReCaptchaInvalidException;

public interface ICaptchaService {

  default void processRequest(final HttpServletRequest request) throws ReCaptchaInvalidException {}

  default void processResponse(final String response, String action) throws ReCaptchaInvalidException {}

  String getReCaptchaSite();

  String getReCaptchaSecret();

}
