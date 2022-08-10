package com.example.verification;

import com.example.verification.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JwtServiceUnitTest {

  private static final String TEST_EMAIL  = "test@test.com";

  private static final String INVALID_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJFbWFpbCBhZGRyZXNzIjoiZXJlbnRoZXdpemFyZEBnbWFpbC5jb20iLCJSZXF1ZXN0ZWQgZGF0ZSI6Ik1vbiBBdWcgMDggMDc6NDU6NTAgR01UIDIwMjIiLCJFeHBpcmVkIGRhdGUiOiJNb24gQXVnIDA4IDA3OjQ1OjUwIEdNVCAyMDIyIiwiZXhwIjoxNjU5OTc0NzUwfQ.QebRd4gDOv98NSQr6jMGZe5wF_h-I-NF83tObsaRlOuBzIFmIxWBAYAn400ds3IJ0cvdNwK95MyPCq0KNFwp5w";

  @Autowired
  private JwtService jwtService;

  @Test
  public void testGeneratedTokenPayload() {
    String jwt = jwtService.generateToken(TEST_EMAIL);
    assertEquals(TEST_EMAIL, jwtService.getEmail(jwt));
  }

  @Test
  public void testValidateValidToken() {
    String jwt = jwtService.generateToken(TEST_EMAIL);
    assertTrue(jwtService.validateToken(jwt));
  }

  @Test
  public void testValidateInvalidToken() {
    assertFalse(jwtService.validateToken(INVALID_JWT));
  }
}
