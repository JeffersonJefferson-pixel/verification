package com.example.verification.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class OtpUtil {
  @Value("${otp.length}")
  private int otpLength;

  @Value("${otp.expiration}")
  private int expiration;

  public String generateOtp() {
    String minOtp = StringUtils.repeat("0", otpLength);
    int maxOtp = (int) Math.pow(10, otpLength) - 1;
    String otpToken = new DecimalFormat(minOtp).format(new Random().nextInt(maxOtp));
    return otpToken;
  }

  public Date calculateExpiryDate() {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, expiration);
    return new Date(cal.getTime().getTime());
  }
}
