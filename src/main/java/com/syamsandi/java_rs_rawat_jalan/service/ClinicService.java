package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicPath;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.CreateClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.clinic.UpdateClinicRequest;

import java.util.List;
import java.util.UUID;


public interface ClinicService {

  ClinicResponse create(User user, CreateClinicRequest request);

  ClinicResponse get(User user, ClinicPath clinicPath);

  List<ClinicResponse> getAll(User user,ClinicPath clinicPath);

  ClinicResponse update(User user, UpdateClinicRequest request);

  void delete(User user,ClinicPath clinicPath);
}
