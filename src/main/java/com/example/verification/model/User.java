package com.example.verification.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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

//  @Column(name = "one_time_password")
//  private String oneTimePassword;
//
//  @Column(name = "otp_requested_time")
//  private Date otpRequestedTime;

  private boolean enabled = false;

//  public boolean isOTPExpired() {
//    long currentTimeInMillis = System.currentTimeMillis();
//    long otpRequestedTimeInMillis = this.otpRequestedTime.getTime();
//
//    if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
//      // OTP expires
//      return false;
//    }
//    return true;
//  }
}