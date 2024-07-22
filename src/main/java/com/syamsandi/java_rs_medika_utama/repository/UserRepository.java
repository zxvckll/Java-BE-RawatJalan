package com.syamsandi.java_rs_medika_utama.repository;

import com.syamsandi.java_rs_medika_utama.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Boolean  existsByEmail(String email);
  Optional<User> findFirstByEmail(String email);
}
