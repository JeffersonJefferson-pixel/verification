package com.example.verification.task;

import java.time.Instant;
import java.util.Date;

import javax.transaction.Transactional;

import com.example.verification.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenPurgeTask {

  @Autowired
  OtpRepository otpRepository;

  @Scheduled(cron= "${purge.cron.expression}")
  public void purgeExpired() {
    System.out.println("Purging OTP");
    Date now = Date.from(Instant.now());

    otpRepository.deleteAllExpiredSince(now);
  }
}
