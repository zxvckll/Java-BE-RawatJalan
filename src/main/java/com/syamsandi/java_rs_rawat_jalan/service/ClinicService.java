package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.ClinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.ClinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;

import java.util.List;
import java.util.UUID;


public interface ClinicService {

  ClinicResponse create(User user, ClinicRequest request);

  ClinicResponse get(User user,String polyclinicSlug,String clinicSlug);

  List<ClinicResponse> getAll(User user,String polyclinicSlug);

  ClinicResponse update(User user,ClinicRequest request,UUID id);

  void delete(User user,UUID polyclinicId,UUID id);
}
