package com.example.verification.captcha;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@NoArgsConstructor
@Getter
@Setter
public class CaptchaSetting {

  private String site;
  private String secret;
}
