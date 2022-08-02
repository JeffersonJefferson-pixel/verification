package com.example.verification.event;

import java.util.Locale;

import com.example.verification.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
  private String appUrl;
  private Locale locale;
  private User user;

  public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
    super(user);

    this.user = user;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
