package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.PolyclinicRequest;
import com.syamsandi.java_rs_rawat_jalan.model.PolyclinicResponse;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;

import java.util.List;
import java.util.UUID;


public interface PolyclinicService {

  PolyclinicResponse create(User user, PolyclinicRequest request);

  PolyclinicResponse get(User user, String slug);

  List<PolyclinicResponse> getAll(User user);

  PolyclinicResponse update(User user, PolyclinicRequest request, UUID id);

  void delete(User user, UUID id);
}
