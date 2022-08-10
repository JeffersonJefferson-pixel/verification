//package com.example.verification.security.jwt;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.persistence.EntityNotFoundException;
//
//import com.example.verification.exception.UserNotFoundException;
//import com.example.verification.model.User;
//import com.example.verification.repository.UserRepository;
//import com.example.verification.service.OtpService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.SignatureException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TokenProvider {
//  private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
//
//  private static final String AUTHORITIES_KEY = "auth";
//
//  @Value("${jwt.secret}")
//  private String secretKey;
//
//  @Value("${jwt.expiration}")
//  private long tokenValidityInSeconds;
//
//  @Autowired
//  private OtpService otpService;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  /**
//   * Create token after verified OTP code
//   *
//   * @param username provided username
//   * @return String token value
//   */
//  public String createTokenAfterVerifiedOtp(String username) throws UserNotFoundException {
//    User user = userRepository.findByUsername(username);
//
//    if (user == null) {
//      throw new UserNotFoundException("user not found");
//    }
//
//
////    List<GrantedAuthority> authorities = user.getRoles()
////                                             .stream()
////                                             .map(role -> new SimpleGrantedAuthority(role.getName()))
////                                             .collect(Collectors.toList());
//
//    Authentication authentication = new UsernamePasswordAuthenticationToken(
//        user.getUsername(), user.getPassword(), new ArrayList<>()
//    );
//
//    return generateToken(authentication);
//  }
//
//  /**
//   * Generate JWT
//   * @param authentication
//   * @return
//   */
//  public String generateToken(Authentication authentication) {
//    String authorities = authentication.getAuthorities().stream()
//                                       .map(GrantedAuthority::getAuthority)
//                                       .collect(Collectors.joining(","));
//    // TODO: integrate Remember Me
//    // in millisecond
//    long now = new Date().getTime();
//    Date expiryDate;
//
//    expiryDate = new Date(now + this.tokenValidityInSeconds * 1000);
//
//    return Jwts.builder()
//        .setSubject(authentication.getName())
//        .claim(AUTHORITIES_KEY, authorities)
//        .signWith(SignatureAlgorithm.HS512, secretKey)
//        .setExpiration(expiryDate)
//        .compact();
//  }
//
//  /**
//   * Validate JWT.
//   * @param jwt
//   * @return whether JWT is valid
//   */
//  public boolean validateToken(String jwt) {
//    try {
//      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
//      return true;
//    } catch (SignatureException e) {
//      log.error("Invalid JWT signature: {}", e.getMessage());
//      return false;
//    }
//  }
//
//  /**
//   * Extract authentication context (payload?) from JWT
//   * @param token
//   * @return
//   */
//  public Authentication getAuthentication(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(secretKey)
//        .parseClaimsJws(token)
//        .getBody();
//
//    String principal = claims.getSubject();
//    Collection<? extends GrantedAuthority> authorities = Arrays
//        .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//        .map(SimpleGrantedAuthority::new)
//        .collect(Collectors.toList());
//
//    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//  }
//
//
//
//}
