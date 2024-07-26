package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
  Optional<Clinic> findFirstById(UUID id);

  Optional<Clinic> findFirstByName(String name);

  Optional<Clinic> findFirstBySlug(String slug);

  List<Clinic> findAllByPolyclinic(Polyclinic polyclinic);

  Boolean existsByName(String name);

}
