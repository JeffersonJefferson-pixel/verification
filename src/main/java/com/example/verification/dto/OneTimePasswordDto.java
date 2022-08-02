package com.example.verification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneTimePasswordDto {

  private String token;

  private UserDto user;
}
