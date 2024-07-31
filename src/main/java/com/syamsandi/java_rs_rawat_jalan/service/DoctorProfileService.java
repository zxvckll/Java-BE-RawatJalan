package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import org.springframework.data.domain.Page;

public interface DoctorProfileService {
  DoctorProfileResponse create (CreateDoctorProfileRequest request, User user);
  DoctorProfileResponse get(DoctorProfilePath doctorProfilePath);

  DoctorProfileResponse update(UpdateDoctorProfileRequest request, User user);

  Page<DoctorProfileResponse> search(SearchDoctorProfileRequest request, DoctorProfilePath doctorProfilePath);

  void delete(User user, DoctorProfilePath doctorProfilePath);
}
