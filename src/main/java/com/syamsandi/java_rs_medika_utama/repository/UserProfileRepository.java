package com.syamsandi.java_rs_medika_utama.repository;

import com.syamsandi.java_rs_medika_utama.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

}
