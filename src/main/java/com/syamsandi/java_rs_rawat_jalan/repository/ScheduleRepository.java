package com.syamsandi.java_rs_rawat_jalan.repository;

import com.syamsandi.java_rs_rawat_jalan.entity.Clinic;
import com.syamsandi.java_rs_rawat_jalan.entity.DoctorProfile;
import com.syamsandi.java_rs_rawat_jalan.entity.Schedule;
import com.syamsandi.java_rs_rawat_jalan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID>, JpaSpecificationExecutor<Schedule> {

  Optional<Schedule> findFirstByDoctorProfileAndId(DoctorProfile doctorProfile,UUID id);

  Optional<Schedule> findAllByDoctorProfile(DoctorProfile doctorProfile);



}
