package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface RoleService {

  RoleResponse create(RoleRequest request);

  RoleResponse get(UUID id);

  List<RoleResponse> getAll();

  RoleResponse update(RoleRequest request,UUID id);

  void delete(UUID id);
}
