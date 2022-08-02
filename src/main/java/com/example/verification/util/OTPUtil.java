package com.example.verification.util;

import java.text.DecimalFormat;
import java.util.Random;

public class OTPUtil {
  public static String generateSimpleOtp() {
    String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
    return otp;
  }
}
