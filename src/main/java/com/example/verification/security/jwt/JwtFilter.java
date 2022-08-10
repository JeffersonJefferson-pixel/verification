package com.example.verification.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.verification.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
//@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    try {
      String jwt = resolveToken(request);
      if (StringUtils.hasText(jwt)) {
        if (jwtService.validateToken(jwt)) {
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtService.getEmail(jwt));

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException eje) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      log.error("Could not set user authentication in security context", e);
    }

  }

  /**
   * Method for resolving token
   * Extract token from request
   * @param request - Http request
   * @return Token string | null
   */
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
//    String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
//    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//      return bearerToken.substring(7, bearerToken.length());
//    }
//    String jwt = request.getParameter(JWTConfigurer.AUTHORIZATION_TOKEN);
//    if (StringUtils.hasText(jwt)) {
//      return jwt;
//    }
//    return null;
  }
}
