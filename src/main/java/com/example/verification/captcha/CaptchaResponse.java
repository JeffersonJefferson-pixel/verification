package com.example.verification.captcha;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@Data
public class CaptchaResponse {

  private boolean success;
  private String hostname;
  private String action;
  @JsonProperty("error-codes")
  private String[] errorCodes;

}
