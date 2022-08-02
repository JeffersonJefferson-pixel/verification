package com.example.verification.repository;

import java.util.Date;

import com.example.verification.model.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
  OneTimePassword findByToken(String token);

  void deleteByExpiryDateLessThan(Date now);

  @Modifying
  @Query("delete from OneTimePassword t where t.expiryDate <= ?1")
  void deleteAllExpiredSince(Date now);
}
