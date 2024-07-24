package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.RoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface RoleService {

  RoleResponse create(User user, RoleRequest request);

  RoleResponse get(User user,UUID id);

  List<RoleResponse> getAll(User user);

  RoleResponse update(User user,RoleRequest request,UUID id);

  void delete(User user,UUID id);
}
