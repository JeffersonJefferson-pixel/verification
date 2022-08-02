package com.example.verification.dto;

//import javax.validation.constraints.NotNull;
//
//import com.example.verification.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
//  @NotNull
  private String username;

//  @ValidEmail
//  @NotNull
  private String email;
}
