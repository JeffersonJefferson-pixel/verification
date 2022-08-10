package com.example.verification.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class OtpDto {

  private String token;

  @Nullable
  private UserDto user;

  @Nullable
  private Date expiryDate;

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Token [String=").append(token).append("]");
    return builder.toString();
  }
}
