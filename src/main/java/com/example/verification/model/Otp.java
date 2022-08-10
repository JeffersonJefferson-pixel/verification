package com.example.verification.model;

import java.util.Date;

import javax.persistence.Entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
/**
 * ref: https://www.baeldung.com/registration-verify-user-by-email
 */
public class Otp {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

//  @Column(unique = true, length = 6)
  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
  private User user;

  private Date expiryDate;

  public Otp(final String token, User user, Date expiryDate) {
    this.token = token;
    this.user = user;
    this.expiryDate = expiryDate;
  }

  /**
   * Compare two instances of OneTimePassword.
   * Compare on password and user.
   * @param obj instance of OneTimePassword to compare
   * @return whether two instances of OneTimePassword is equal.
   */
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

    final Otp other = (Otp) obj;

    if (getExpiryDate() == null) {
      if (other.getExpiryDate() != null) {
        return false;
      }
    } else if (!getExpiryDate().equals(other.getExpiryDate())) {
      return false;
    }

    if (getToken() == null) {
      if (other.getToken() != null) {
        return false;
      }
    } else if (!getToken().equals(other.getToken())) {
      return false;
    }

    if (getUser() == null) {
      if (other.getUser() != null) {
        return false;
      }
    } else if (!getUser().equals(other.getUser())) {
      return false;
    }
    return true;
  }

//  private Date calculateExpiryDate(final int expiryTimeInMinutes) {
//    final Calendar cal = Calendar.getInstance();
//    cal.setTimeInMillis(new Date().getTime());
//    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//    return new Date(cal.getTime().getTime());
//  }

//  /**
//   * Change token of an OTP instance and extend the expiry date.
//   * @param token new password to change.
//   */
//  public void updateToken(final String token) {
//    this.token = token;
//    this.expiryDate = calculateExpiryDate(EXPIRATION);
//  }
}
