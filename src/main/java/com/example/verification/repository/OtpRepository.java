package com.example.verification.repository;

import java.util.Date;

import com.example.verification.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OtpRepository extends JpaRepository<Otp, Long> {

  Otp findByTokenAndExpiryDateGreaterThan(String token, Date date);

  Otp findByToken(String token);

  void deleteByExpiryDateLessThan(Date now);

  @Modifying
  @Query("delete from Otp t where t.expiryDate <= ?1")
  void deleteAllExpiredSince(Date now);
}
