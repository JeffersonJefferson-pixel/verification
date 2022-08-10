package com.example.verification.security.jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.verification.exception.UserNotFoundException;
import com.example.verification.model.User;
import com.example.verification.repository.UserRepository;
import com.example.verification.security.ecdsa.KeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class JwtService {
  private static final String EMAIL_ADDRESS = "Email address";
  private static final String REQUESTED_DATE = "Requested date";
  private static final String EXPIRED_DATE = "Expired date";

  private final Logger log = LoggerFactory.getLogger(JwtService.class);

//  private final KeyProvider keyProvider;
  @Autowired
  private KeyProvider keyProvider;

  @Value("${jwt.expiration}")
  private long tokenValidityInSeconds;

  /**
   * Generate JWT
   * @param email
   * @return
   */
  public String generateToken(String email) {
    // TODO: integrate Remember Me
    // in millisecond
    Date date = new Date();
    long now = date.getTime();
    Date expiryDate;

    expiryDate = new Date(now + this.tokenValidityInSeconds * 1000);

    Map header = new HashMap<String,Object>();
    header.put("typ", "JWT");

    return Jwts.builder()
        .setHeader(header)
        .claim(EMAIL_ADDRESS, email)
        .claim(REQUESTED_DATE, date.toString())
        .claim(EXPIRED_DATE, date.toString())
        .signWith(SignatureAlgorithm.ES256, keyProvider.getPrivateKey())
        .setExpiration(expiryDate)
        .compact();
  }

  /**
   * Validate JWT.
   * @param jwt
   * @return whether JWT is valid
   */
  public boolean validateToken(String jwt) {
    try {
      Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(jwt);
      return true;
    } catch (SignatureException | ExpiredJwtException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Extract email from jwt claims
   */
  public String getEmail(String jwt) {
    return (String) getClaims(jwt).get(EMAIL_ADDRESS);
  }

  /**
   * Extract claims form jwt
   */
  public Claims getClaims(String jwt) {
    return Jwts.parser()
        .setSigningKey(keyProvider.getPublicKey())
        .parseClaimsJws(jwt)
        .getBody();
  }
}
