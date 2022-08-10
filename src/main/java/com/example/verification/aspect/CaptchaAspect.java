package com.example.verification.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.verification.captcha.CaptchaService;
import com.example.verification.exception.ReCaptchaInvalidException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class CaptchaAspect {

  @Autowired
  private CaptchaService captchaService;

  @Around("@annotation(RequireCaptcha)")
  public Object validateCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    try {
      captchaService.processRequest(request);
    } catch (ReCaptchaInvalidException e) {
      Map<String, String> body = new HashMap<>();
      body.put("message", e.getMessage());
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    return joinPoint.proceed();
  }
}
