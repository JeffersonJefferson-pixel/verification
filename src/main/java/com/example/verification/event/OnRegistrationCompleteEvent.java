package com.example.verification.event;

import java.util.Locale;

import com.example.verification.dto.UserDto;
import com.example.verification.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private String appUrl;
  private Locale locale;
  private UserDto userDto;

  public OnRegistrationCompleteEvent(UserDto userDto, Locale locale, String appUrl) {
    super(userDto);

    this.userDto = userDto;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
