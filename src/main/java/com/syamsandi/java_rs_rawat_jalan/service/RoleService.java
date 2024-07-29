package com.syamsandi.java_rs_rawat_jalan.service;

import com.syamsandi.java_rs_rawat_jalan.entity.User;
import com.syamsandi.java_rs_rawat_jalan.model.role.CreateRoleRequest;
import com.syamsandi.java_rs_rawat_jalan.model.role.RoleResponse;

import java.util.List;
import java.util.UUID;


public interface RoleService {

  RoleResponse create(User user, CreateRoleRequest request);

  RoleResponse get(User user,UUID id);

  List<RoleResponse> getAll(User user);

  RoleResponse update(User user, CreateRoleRequest request, UUID id);

  void delete(User user,UUID id);
}
