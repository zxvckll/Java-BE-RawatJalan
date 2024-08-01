package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.doctor_profile.*;
import com.syamsandi.java_rs_rawat_jalan.model.patient_profile.*;
import org.springframework.data.domain.Page;

public interface PatientProfileService {
  PatientProfileResponse create (CreatePatientProfileRequest request, User user);
  PatientProfileResponse get(PatientProfilePath doctorProfilePath, User user);

  PatientProfileResponse update(UpdatePatientProfileRequest request, User user);

  Page<PatientProfileResponse> search(SearchPatientProfileRequest request);

  void delete(User user, PatientProfilePath doctorProfilePath);
}
