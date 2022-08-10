package com.example.verification.dto;

//import javax.validation.constraints.NotNull;
//
//import com.example.verification.validation.ValidEmail;
import com.example.verification.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class UserDto {
  private Long id;

//  @ValidEmail
  private String email;

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("UserDto [email=")
           .append(email).append("]");
    return builder.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UserDto user = (UserDto) obj;
    if (!getEmail().equals(user.getEmail())) {
      return false;
    }
    return true;
  }
}
