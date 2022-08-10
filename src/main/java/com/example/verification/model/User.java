package com.example.verification.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  private static final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String username;

  private String email;

  private String password;

  private boolean enabled = false;

  private String secret;

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
    final User user = (User) obj;
    if (!getEmail().equals(user.getEmail())) {
      return false;
    }
    return true;
  }
}