package com.example.verification.exception;

public class ReCaptchaUnavailableException extends RuntimeException {

  public ReCaptchaUnavailableException() {
    super();
  }

  public ReCaptchaUnavailableException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
