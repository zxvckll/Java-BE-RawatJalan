package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.PatientProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID>, JpaSpecificationExecutor<PatientProfile> {
  Optional<PatientProfile> findFirstByUser(User user);

  Optional<PatientProfile> findFirstBySlugAndId(String slug,UUID id);
}
