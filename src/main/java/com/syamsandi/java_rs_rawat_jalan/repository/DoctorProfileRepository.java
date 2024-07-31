package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.DoctorProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.Polyclinic;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.DoctorProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID>, JpaSpecificationExecutor<DoctorProfile> {

  Optional<DoctorProfile> findFirstByClinicAndIdAndSlug(Clinic clinic,UUID id, String slug);

  Optional<DoctorProfile> findFirstByUser(User user);


}
