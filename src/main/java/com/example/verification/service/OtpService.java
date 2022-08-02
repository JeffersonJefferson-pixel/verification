package com.example.verification.service;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import com.example.verification.model.OneTimePassword;
import com.example.verification.model.User;
import com.example.verification.repository.OneTimePasswordRepository;
import com.example.verification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OtpService {
  @Autowired
  private OneTimePasswordRepository oneTimePasswordRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Generate an OTP for a user.
   * @param user user to associate the generated OTP with
   * @return the OTP created
   * @throws MessagingException
   * @throws UnsupportedEncodingException
   */
  public OneTimePassword generateOtpForUser(User user) throws MessagingException, UnsupportedEncodingException {
    // attach an OTP to a user
    String token = new DecimalFormat("000000").format(new Random().nextInt(999999));
    final OneTimePassword otp = new OneTimePassword(token, user);

    return oneTimePasswordRepository.save(otp);
  }

  /**
   * Verify OTP. Activate the associated user if OTP is valid
   * @param token OTP token
   * @return whether OTP is valid
   */
  public boolean verifyOtp(String otpToken) {
    final OneTimePassword foundOtp = oneTimePasswordRepository.findByToken(otpToken);
    if (foundOtp == null) {
      return false;
    }

    final User user = foundOtp.getUser();
    final Calendar cal = Calendar.getInstance();
    if ((foundOtp.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      // delete OTP if expired
      oneTimePasswordRepository.delete(foundOtp);
      return false;
    }

    // activate user
    user.setEnabled(true);
    oneTimePasswordRepository.delete(foundOtp);
    userRepository.save(user);
    return true;
  }
}
