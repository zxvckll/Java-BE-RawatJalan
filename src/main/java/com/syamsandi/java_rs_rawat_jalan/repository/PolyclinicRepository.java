package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import com.syamsandi.java_rs_rawat_jalan.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolyclinicRepository extends JpaRepository<Polyclinic, UUID> {


  Optional<Polyclinic> findFirstByName(String name);

  Optional<Polyclinic> findFirstBySlugAndId(String slug, UUID id);

  Boolean existsByClinics(Clinic clinic);

  Boolean existsByName(String name);

}
