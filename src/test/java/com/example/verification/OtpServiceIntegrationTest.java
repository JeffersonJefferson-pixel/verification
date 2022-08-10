package com.example.verification;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.annotation.Resource;

import com.example.verification.dto.UserDto;
import com.example.verification.exception.OtpInvalidException;
import com.example.verification.exception.UserAlreadyExistException;
import com.example.verification.exception.UserNotFoundException;
import com.example.verification.mapper.MapStructMapper;
import com.example.verification.model.Otp;
import com.example.verification.model.User;
import com.example.verification.repository.OtpRepository;
import com.example.verification.repository.UserRepository;
import com.example.verification.service.OtpService;
import com.example.verification.service.UserService;
import com.example.verification.util.OtpUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/verification-test"
    }
)
@TestInstance(Lifecycle.PER_CLASS)
public class OtpServiceIntegrationTest {

  private static final String TEST_EMAIL  = "test@test.com";
  private static final String TEST_NONEXISTENT_EMAIL  = "test_nonexistent@test.com";

  @Autowired
  private OtpService otpService;

  @Autowired
  private OtpUtil otpUtil;

  @Autowired
  private OtpRepository otpRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MapStructMapper mapper;

  private User testUser;
  private UserDto testUserDto;

  private User testNonExistentUser;
  private UserDto testNonExistentUserDto;

  @BeforeAll
  public void setup() {
    testUser = new User();
    testUser.setEmail(TEST_EMAIL);
    userRepository.save(testUser);
    testUserDto = mapper.userToUserDto(testUser);

    testNonExistentUser = new User();
    testNonExistentUser.setEmail(TEST_NONEXISTENT_EMAIL);
    testNonExistentUserDto = mapper.userToUserDto(testNonExistentUser);
  }

  @AfterAll
  public void teardown() {
    otpRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void givenUserAndOtp_whenLoadOtp_thenFound() throws UserNotFoundException {
    Otp generatedOtp = otpService.generateOtpForUser(testUserDto);
    Otp foundOtp = otpRepository.findByToken(generatedOtp.getToken());

    assertNotNull(foundOtp);
    assertEquals(generatedOtp, foundOtp);
  }

  @Test
  public void givenValidOtp_whenVerify_thenSucceed() {
    String otpToken = otpUtil.generateOtp();
    Otp generatedOtp = new Otp(otpToken, testUser, null);
    generatedOtp.setExpiryDate(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));
    otpRepository.save(generatedOtp);

    try {
      otpService.verifyOtp(generatedOtp.getToken());
      assertTrue(true);
    } catch (OtpInvalidException e) {
      fail();
    }
  }

  @Test
  public void givenExpiredOtp_whenVerify_thenFails() {
    String otpToken = otpUtil.generateOtp();
    Otp generatedOtp = new Otp(otpToken, testUser, null);
    // make otp expire
    generatedOtp.setExpiryDate(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
    otpRepository.save(generatedOtp);

    try {
      otpService.verifyOtp(generatedOtp.getToken());
      fail();
    } catch (OtpInvalidException e) {
      assertTrue(true);
    }
  }

  @Test
  public void givenNonexistentOtp_whenVerify_thenFail() {
    String generatedOtp = otpUtil.generateOtp();
    // ensure test passes
    otpRepository.deleteAll();
    try {
      otpService.verifyOtp(generatedOtp);
      fail();
    } catch (OtpInvalidException e) {
      assertTrue(true);
    }
  }

  @Test
  public void givenNonExistentUser_whenGenerateOtpForUser_thenFail() {
    try {
      otpService.generateOtpForUser(testNonExistentUserDto);
      fail();
    } catch (UserNotFoundException e) {
      assertTrue(true);
    }
  }

  @Test
  public void givenValidOtp_whenVerify_thenDeleted() throws OtpInvalidException {
    String otpToken = otpUtil.generateOtp();
    Otp generatedOtp = new Otp(otpToken, testUser, null);
    generatedOtp.setExpiryDate(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));
    otpRepository.save(generatedOtp);
    otpService.verifyOtp(generatedOtp.getToken());

    Otp foundOtp = otpRepository.findByToken(otpToken);
    assertNull(foundOtp);
  }
}
