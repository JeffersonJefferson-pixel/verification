package com.example.verification.captcha;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.stereotype.Service;
import com.google.common.cache.LoadingCache;

@Service
public class ReCaptchaAttemptService {
  private final int MAX_ATTEMPT = 4;
  private LoadingCache<String, Integer> attemptsCache;

  public ReCaptchaAttemptService() {
    attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
      @Override
      public Integer load(String s) throws Exception {
        return null;
      }
    });
  }

  public void reCaptchaSucceeded(final String key) {
    attemptsCache.invalidate(key);
  }

  public void reCaptchaFailed(final String key) {
    Integer attempts = attemptsCache.getIfPresent(key);
    if (attempts == null) {
      attemptsCache.put(key, 0);
    }else {
      attempts++;
      attemptsCache.put(key, attempts);
    }
  }

  public boolean isBlocked(final String key) {
    Integer attempts = attemptsCache.getIfPresent(key);
    if (attempts == null) {
      return false;
    } else {
      return  attempts >= MAX_ATTEMPT;
    }
  }
}
