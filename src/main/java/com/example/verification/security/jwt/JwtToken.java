package com.example.verification.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JwtToken {

  private String idToken;

  public JwtToken(String idToken) {
    this.idToken = idToken;
  }

  @JsonProperty("id_token")
  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

}
