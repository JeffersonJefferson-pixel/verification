package com.example.verification.repository;

import com.example.verification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);

  User findByUsername(String username);
}
