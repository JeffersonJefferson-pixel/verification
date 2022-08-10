package com.example.verification.service;

import java.time.Instant;
import java.util.Date;

import javax.transaction.Transactional;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.OtpInvalidException;
import com.example.verification.exception.UserNotFoundException;
import com.example.verification.mapper.MapStructMapper;
import com.example.verification.model.Otp;
import com.example.verification.model.User;
import com.example.verification.repository.OtpRepository;
import com.example.verification.repository.UserRepository;
import com.example.verification.util.OtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OtpService {

  private final Logger LOGGER = LoggerFactory.getLogger(OtpService.class);

  @Autowired
  private OtpRepository otpRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MapStructMapper mapper;

  @Autowired
  private OtpUtil otpUtil;

  /**
   * Generate an OTP for a user.
   * @param userDto user to associate the generated OTP with
   * @return the OTP created
   */
  public Otp generateOtpForUser(UserDto userDto) throws UserNotFoundException {
    LOGGER.debug("Generating OTP for user");
    // attach an OTP to a user
    User foundUser = userRepository.findByEmail(userDto.getEmail());
    if (foundUser == null) {
      throw new UserNotFoundException("user not found.");
    }
    final Otp otp = new Otp(otpUtil.generateOtp(), foundUser, otpUtil.calculateExpiryDate());

    return otpRepository.save(otp);
  }

  /**
   * Verify OTP. Activate the associated user if OTP is valid
   * @param otpToken OTP token
   * @return whether OTP is valid
   */
  public Otp verifyOtp(String otpToken) throws OtpInvalidException {
    // Same tokens might exist
    // only get the one that hasn't expired (assume this is unique)
    Date now = Date.from(Instant.now());
    final Otp foundOtp = otpRepository.findByTokenAndExpiryDateGreaterThan(otpToken, now);
    if (foundOtp == null) {
      throw new OtpInvalidException("Otp not found.");
    }

    final User user = foundOtp.getUser();

    // activate user
    user.setEnabled(true);
    otpRepository.delete(foundOtp);
    userRepository.save(user);
    return foundOtp;
  }
}
